package me.laysar.bastionhelper.client.handler;

import com.mojang.datafixers.util.Either;
import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import me.laysar.bastionhelper.client.render.BlockRenderer;
import me.laysar.bastionhelper.client.render.PathRenderer;
import me.laysar.bastionhelper.client.render.RenderGroup;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler.PiglinAggroLevel;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ShowPiglinPathsHandler {
	public static boolean showPaths = false;

	private static class TargetLocationRenderer {
		public final BlockPos blockPos;
		public final BlockRenderer renderer;
		private final Map<Integer, PiglinAggroLevel> piglinAggroLevels = new HashMap<>();
		private final Map<PiglinAggroLevel, MutableInt> aggroLevelCounts;

		public TargetLocationRenderer(BlockPos position) {
			this.blockPos = position;
			this.renderer = new BlockRenderer(position, Color.WHITE);
			aggroLevelCounts = Arrays.stream(PiglinAggroLevel.values()).collect(Collectors.toMap(lvl -> lvl, _l -> new MutableInt(0)));
		}

		public void setAggroLevel(int id, @NotNull PiglinAggroLevel newAggroLevel) {
			PiglinAggroLevel prevAggroLevel = piglinAggroLevels.put(id, newAggroLevel);
			if (prevAggroLevel == newAggroLevel) return;

			if (prevAggroLevel != null)
				aggroLevelCounts.get(prevAggroLevel).decrement();
			aggroLevelCounts.get(newAggroLevel).increment();
			updateColor();
		}
		public boolean removeAggroLevel(int id) {
			PiglinAggroLevel removedAggroLevel = piglinAggroLevels.remove(id);
			if (removedAggroLevel == null) return false;

			aggroLevelCounts.get(removedAggroLevel).decrement();
			if (aggroLevelCounts.values().stream().allMatch(v -> v.intValue() == 0)) return true;
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
			this.renderer.setColor(colorOf(maxAggroLevel.get()));
		}
	}
	private static final RenderGroup<BlockRenderer> targetRenderGroup = new RenderGroup<>(50, RenderGroup.RenderOption.FRONT);
	private static final RenderGroup<PathRenderer> pathfindingRenderGroup = new RenderGroup<>(50, RenderGroup.RenderOption.FRONT);

	private static final Map<Integer, PathRenderer> piglinPathRenderers = new HashMap<>();
	private static final Map<Integer, BlockPos> piglinTargets = new HashMap<>();

	private static final Map<BlockPos, TargetLocationRenderer> targetLocationRenderers = new HashMap<>();


	private record CreateAction(int id, int currentNodeIndex, BlockPos[] positions, BlockPos target, PiglinAggroLevel aggroLevel) {}
	private record UpdateAction(int id, int currentNodeIndex, PiglinAggroLevel aggroLevel) {}
	private static final List<Either<CreateAction, UpdateAction>> queuedActions = Collections.synchronizedList(new ArrayList<>());

	public static void run() {
		ClientEventEmitter.showPiglinPaths();

		showPaths = !showPaths;
		if (!showPaths)
			clear();
	}

	public static void create(@NotNull PacketContext ctx, @NotNull PacketByteBuf buf) {
		if (!showPaths) return;

		int id = buf.readInt();
		int currentNodeIndex = buf.readInt();
		int length = buf.readInt();

		BlockPos[] positions = new BlockPos[length];
		for (int i = 0; i < length; i++)
			positions[i] = buf.readBlockPos();

		BlockPos target = buf.readBlockPos();

		PiglinAggroLevel aggroLevel = PiglinAggroLevel.fromInt(buf.readInt());

		queuedActions.add(Either.left(new CreateAction(id, currentNodeIndex, positions, target, aggroLevel)));
	}

	public static void update(@NotNull PacketContext ctx, @NotNull PacketByteBuf buf) {
		if (!showPaths) return;

		int id = buf.readInt();
		int currentNodeIndex = buf.readInt();

		PiglinAggroLevel aggroLevel;
		try {
			aggroLevel = PiglinAggroLevel.fromInt(buf.readInt());
		} catch (IndexOutOfBoundsException _e) {
			aggroLevel = PiglinAggroLevel.NONE;
		}

		queuedActions.add(Either.right(new UpdateAction(id, currentNodeIndex, aggroLevel)));
	}

	private static void processQueue() {
		synchronized (queuedActions) {
			for (Either<CreateAction, UpdateAction> either : queuedActions) {
				either.map(ShowPiglinPathsHandler::createQueued, ShowPiglinPathsHandler::updateQueued);
			}
			queuedActions.clear();
		}
	}

	private static @Nullable Object createQueued(@NotNull CreateAction ac) {
		createPath(ac.id, ac.positions, ac.target, ac.currentNodeIndex, ac.aggroLevel);
		createTarget(ac.id, ac.target, ac.aggroLevel);
		return null;
	}

	private static @Nullable Object updateQueued(@NotNull UpdateAction ac) {
		if (ac.currentNodeIndex < 0) {
			removePath(ac.id);
			removeTarget(ac.id);
		} else {
			updatePath(ac.id, ac.currentNodeIndex, ac.aggroLevel);
			updateTarget(ac.id, ac.aggroLevel);
		}
		return null;
	}

	private static void createPath(int id, @NotNull BlockPos[] positions, @NotNull BlockPos target, int currentNodeIndex, PiglinAggroLevel aggroLevel) {
		if (!showPaths) return;

		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) return;
		if (!(world.getEntityById(id) instanceof LivingEntity entity)) return;

		removePath(id);

		PathRenderer newRenderer = new PathRenderer(entity, positions, currentNodeIndex, colorOf(aggroLevel));
		piglinPathRenderers.put(id, newRenderer);
		pathfindingRenderGroup.add(newRenderer);
	}

	private static void updatePath(int id, int currentNodeIndex, @NotNull PiglinAggroLevel aggroLevel) {
		if (!showPaths) return;

		PathRenderer renderer = piglinPathRenderers.get(id);
		if (renderer == null) return;

		renderer.setCurrentNodeIndex(currentNodeIndex);
		renderer.setColor(colorOf(aggroLevel));
	}

	private static void removePath(int id) {
		if (!showPaths) return;

		PathRenderer prevRenderer = piglinPathRenderers.remove(id);
		pathfindingRenderGroup.remove(prevRenderer);
		removeTarget(id);
	}

	private static void createTarget(int id, @NotNull BlockPos target, @NotNull PiglinAggroLevel aggroLevel) {
		if (!showPaths) return;

		TargetLocationRenderer existingRenderer = targetLocationRenderers.get(target);
		if (existingRenderer != null && existingRenderer.blockPos.equals(target)) {
			updateTarget(id, aggroLevel);
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

	private static void updateTarget(int id, @NotNull PiglinAggroLevel newAggroLevel) {
		if (!showPaths) return;

		BlockPos target = piglinTargets.get(id);
		if (target == null) return;

		TargetLocationRenderer renderer = targetLocationRenderers.get(target);
		assert renderer != null;

		renderer.setAggroLevel(id, newAggroLevel);
	}

	private static void removeTarget(int id) {
		if (!showPaths) return;

		BlockPos removedPiglinTarget = piglinTargets.remove(id);
		if (removedPiglinTarget == null) return;

		TargetLocationRenderer info = targetLocationRenderers.get(removedPiglinTarget);
		if (info == null) return;

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
		if (!showPaths) return;

		pathfindingRenderGroup.render();
		targetRenderGroup.render();

		processQueue();
	}

	@Contract(pure = true)
	private static Color colorOf(@NotNull PiglinAggroLevel aggroLevel) {
		return switch (aggroLevel) {
			case NONE -> new Color(1.0f, 1.0f, 1.0f, 0.5f);
			case LIGHT -> Color.YELLOW;
			case MEDIUM -> Color.ORANGE;
			case HEAVY -> Color.RED;
			case GOLD_DISTRACTED -> Color.GREEN;
		};
	}
}
