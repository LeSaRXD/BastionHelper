package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.render.BlockOutlineRenderer;
import me.laysar.bastionhelper.client.render.PathRenderer;
import me.laysar.bastionhelper.client.render.RenderGroup;
import me.laysar.bastionhelper.handler.AggroLevelsHandler.PiglinAggroLevel;
import me.laysar.bastionhelper.network.packets.S2CCreatePiglinPath;
import me.laysar.bastionhelper.network.packets.S2CRemovePiglinPath;
import me.laysar.bastionhelper.network.packets.S2CUpdatePiglinPath;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ShowPiglinPathsHandler {
	public static boolean showPaths = false;

	private static class TargetLocationRenderer {
		public final BlockPos blockPos;
		public final BlockOutlineRenderer renderer;
		private final Map<Integer, PiglinAggroLevel> piglinAggroLevels = new HashMap<>();
		private final Map<PiglinAggroLevel, MutableInt> aggroLevelCounts;

		public TargetLocationRenderer(BlockPos position) {
			this.blockPos = position;
			this.renderer = new BlockOutlineRenderer(position, Color.WHITE);
			aggroLevelCounts = Arrays.stream(PiglinAggroLevel.values()).collect(Collectors.toMap(lvl -> lvl, _l -> new MutableInt(0)));
		}

		public void setAggroLevel(int id, @NotNull PiglinAggroLevel newAggroLevel) {
			PiglinAggroLevel prevAggroLevel = piglinAggroLevels.put(id, newAggroLevel);
			if (prevAggroLevel == newAggroLevel) {
				return;
			}

			if (prevAggroLevel != null) {
				aggroLevelCounts.get(prevAggroLevel).decrement();
			}
			aggroLevelCounts.get(newAggroLevel).increment();
			updateColor();
		}

		public boolean removeAggroLevel(int id) {
			PiglinAggroLevel removedAggroLevel = piglinAggroLevels.remove(id);
			if (removedAggroLevel == null) {
				return false;
			}

			aggroLevelCounts.get(removedAggroLevel).decrement();
			if (aggroLevelCounts.values().stream().allMatch(v -> v.intValue() == 0)) {
				return true;
			}
			updateColor();
			return false;
		}

		private void updateColor() throws IndexOutOfBoundsException {
			Comparator<Map.Entry<PiglinAggroLevel, MutableInt>> comp = Comparator
					.<Map.Entry<PiglinAggroLevel, MutableInt>>comparingInt(e -> e.getValue().intValue())
					.thenComparingInt((e) -> e.getKey().toInt());

			Optional<PiglinAggroLevel> maxAggroLevel = aggroLevelCounts
					.entrySet()
					.stream()
					.max(comp)
					.map(Map.Entry::getKey);

			assert maxAggroLevel.isPresent();
			this.renderer.setColor(maxAggroLevel.get().toColor());
		}
	}

	private static final RenderGroup<BlockOutlineRenderer> targetRenderGroup = new RenderGroup<>(50, RenderGroup.RenderOption.FRONT);
	private static final RenderGroup<PathRenderer> pathfindingRenderGroup = new RenderGroup<>(50, RenderGroup.RenderOption.FRONT);

	private static final Map<Integer, PathRenderer> piglinPathRenderers = new HashMap<>();
	private static final Map<Integer, BlockPos> piglinTargets = new HashMap<>();

	private static final Map<BlockPos, TargetLocationRenderer> targetLocationRenderers = new HashMap<>();

	private record Action(@Nullable S2CCreatePiglinPath create, @Nullable S2CUpdatePiglinPath update,
						  @Nullable S2CRemovePiglinPath remove) {
		public Action(@NotNull S2CCreatePiglinPath create) {
			this(create, null, null);
		}

		public Action(@NotNull S2CUpdatePiglinPath update) {
			this(null, update, null);
		}

		public Action(@NotNull S2CRemovePiglinPath remove) {
			this(null, null, remove);
		}

		public void apply() {
			if (create != null) {
				createPath(create.id(), create.positions(), create.currentNodeIndex(), create.target());
			} else if (update != null) {
				updatePath(update.id(), update.currentNodeIndex());
			} else if (remove != null) {
				removePath(remove.id());
			}
		}
	}

	private static final List<Action> queuedActions = Collections.synchronizedList(new ArrayList<>());

	public static void run(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		toggle();
	}

	public static void toggle() {
		showPaths = !showPaths;
	}

	public static void create(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		queuedActions.add(new Action(S2CCreatePiglinPath.fromBuf(buf)));
	}

	public static void update(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		queuedActions.add(new Action(S2CUpdatePiglinPath.fromBuf(buf)));
	}

	public static void remove(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		queuedActions.add(new Action(S2CRemovePiglinPath.fromBuf(buf)));
	}

	private static void processQueue() {
		synchronized (queuedActions) {
			queuedActions.forEach(Action::apply);
			queuedActions.clear();
		}
	}

	private static void createPath(int id, @NotNull BlockPos[] positions, int currentNodeIndex, @NotNull BlockPos target) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			return;
		}
		if (!(world.getEntityById(id) instanceof LivingEntity entity)) {
			return;
		}

		removePath(id);

		PiglinAggroLevel aggroLevel = AggroLevelsHandler.getAggroLevel(id);
		PathRenderer newRenderer = new PathRenderer(entity, positions, currentNodeIndex, aggroLevel.toColor());
		piglinPathRenderers.put(id, newRenderer);
		pathfindingRenderGroup.add(newRenderer);
		createTarget(id, target, aggroLevel);
	}

	private static void updatePath(int id, int currentNodeIndex) {
		PathRenderer renderer = piglinPathRenderers.get(id);
		if (renderer == null) {
			return;
		}

		renderer.setCurrentNodeIndex(currentNodeIndex);
		PiglinAggroLevel aggroLevel = AggroLevelsHandler.getAggroLevel(id);
		renderer.setColor(aggroLevel.toColor());
	}

	public static void removePath(int id) {
		PathRenderer prevRenderer = piglinPathRenderers.remove(id);
		pathfindingRenderGroup.remove(prevRenderer);
		removeTarget(id);
	}

	public static void refreshPath(int id, @NotNull PiglinAggroLevel aggroLevel) {
		PathRenderer pathRenderer = piglinPathRenderers.get(id);
		if (pathRenderer == null) {
			return;
		}
		pathRenderer.setColor(aggroLevel.toColor());

		BlockPos target = piglinTargets.get(id);
		if (target == null) {
			return;
		}

		TargetLocationRenderer targetRenderer = targetLocationRenderers.get(target);
		assert targetRenderer != null;

		targetRenderer.setAggroLevel(id, aggroLevel);
	}

	private static void createTarget(int id, @NotNull BlockPos target, @NotNull PiglinAggroLevel aggroLevel) {
		TargetLocationRenderer existingRenderer = targetLocationRenderers.get(target);
		if (existingRenderer != null && existingRenderer.blockPos.equals(target)) {
			updateTarget(id);
			return;
		}

		removeTarget(id);

		piglinTargets.put(id, target);
		if (!targetLocationRenderers.containsKey(target)) {
			TargetLocationRenderer newRenderer = new TargetLocationRenderer(target);
			targetLocationRenderers.put(target, newRenderer);
			targetRenderGroup.add(newRenderer.renderer);
		}
		targetLocationRenderers.get(target).setAggroLevel(id, aggroLevel);
	}

	private static void updateTarget(int id) {
		BlockPos target = piglinTargets.get(id);
		if (target == null) {
			return;
		}

		TargetLocationRenderer renderer = targetLocationRenderers.get(target);
		assert renderer != null;

		PiglinAggroLevel aggroLevel = AggroLevelsHandler.getAggroLevel(id);
		renderer.setAggroLevel(id, aggroLevel);
	}

	private static void removeTarget(int id) {
		BlockPos removedPiglinTarget = piglinTargets.remove(id);
		if (removedPiglinTarget == null) {
			return;
		}

		TargetLocationRenderer info = targetLocationRenderers.get(removedPiglinTarget);
		if (info == null) {
			return;
		}

		boolean isEmpty = info.removeAggroLevel(id);
		if (isEmpty) {
			targetLocationRenderers.remove(removedPiglinTarget);
			targetRenderGroup.remove(info.renderer);
		}
	}

	public static void clear() {
		showPaths = false;

		piglinPathRenderers.clear();
		piglinTargets.clear();

		pathfindingRenderGroup.clear();
		targetRenderGroup.clear();
		targetLocationRenderers.clear();
		queuedActions.clear();
	}

	public static void render() {
		if (!showPaths) {
			return;
		}

		pathfindingRenderGroup.render();
		targetRenderGroup.render();

		processQueue();
	}
}
