package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import me.laysar.bastionhelper.client.render.PathRenderer;
import me.laysar.bastionhelper.client.render.RenderGroup;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler.PiglinAggroLevel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShowPiglinPathsHandler {
	public static boolean showPaths = false;

	private static final RenderGroup<PathRenderer> pathfindingRenderGroup = new RenderGroup<>(10, RenderGroup.RenderOption.FRONT);
	private static final Map<Integer, PathRenderer> piglinPathRenderers = new HashMap<>();

	public static void run() {
		ClientEventEmitter.showPiglinPaths();

		showPaths = !showPaths;
		if (!showPaths) {
			clear();
			return;
		}

		for (PathRenderer renderer : piglinPathRenderers.values()) {
			pathfindingRenderGroup.add(renderer);
		}
	}

	public static void create(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		int currentNodeIndex = buf.readInt();
		int length = buf.readInt();

		BlockPos[] positions = new BlockPos[length];
		for (int i = 0; i < length; i++)
			positions[i] = buf.readBlockPos();

		BlockPos target = buf.readBlockPos();

		PiglinAggroLevel aggroLevel = PiglinAggroLevel.fromInt(buf.readInt());

		addPath(id, positions, target, currentNodeIndex, aggroLevel);
	}

	public static void update(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		int currentNodeIndex = buf.readInt();

		if (currentNodeIndex < 0) {
			removePath(id);
			return;
		}

		PiglinAggroLevel aggroLevel = PiglinAggroLevel.fromInt(buf.readInt());

		updatePath(id, currentNodeIndex, aggroLevel);
	}

	private static void addPath(int id, @NotNull BlockPos[] positions, @NotNull BlockPos target, int currentNodeIndex, PiglinAggroLevel aggroLevel) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) return;
		if (!(world.getEntityById(id) instanceof LivingEntity entity)) return;

		removePath(id);

		PathRenderer newRenderer = new PathRenderer(entity, positions, target, currentNodeIndex, colorOf(aggroLevel));
		piglinPathRenderers.put(id, newRenderer);
		pathfindingRenderGroup.add(newRenderer);
	}

	private static void updatePath(int id, int currentNodeIndex, PiglinAggroLevel aggroLevel) {
		PathRenderer renderer = piglinPathRenderers.get(id);
		if (renderer == null) return;

		renderer.setCurrentNodeIndex(currentNodeIndex);
		renderer.setColor(colorOf(aggroLevel));
	}

	private static void removePath(int id) {
		PathRenderer prevRenderer = piglinPathRenderers.remove(id);
		pathfindingRenderGroup.remove(prevRenderer);
	}

	public static void clear() {
		pathfindingRenderGroup.clear();
		piglinPathRenderers.clear();
		showPaths = false;
	}

	public static void render() {
		pathfindingRenderGroup.render();
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
