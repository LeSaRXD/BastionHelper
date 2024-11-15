package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import me.laysar.bastionhelper.client.render.PathRenderer;
import me.laysar.bastionhelper.client.render.RenderGroup;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

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

	public static void update(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		int id = buf.readInt();

		int pathLength = buf.readInt();

		if (pathLength <= 0) {
			removePath(id);
			return;
		}

		BlockPos[] positions = new BlockPos[pathLength];
		for (int i = 0; i < pathLength; i++)
			positions[i] = buf.readBlockPos();

		BlockPos target = buf.readBlockPos();

		addPath(id, positions, target);
	}

	private static void addPath(int id, @NotNull BlockPos[] positions, @NotNull BlockPos target) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) return;
		if (!(world.getEntityById(id) instanceof LivingEntity entity)) return;

		PathRenderer prevRenderer = piglinPathRenderers.get(id);
		if (prevRenderer != null) pathfindingRenderGroup.remove(prevRenderer);

		PathRenderer newRenderer = new PathRenderer(entity, positions, Color.YELLOW, target, Color.WHITE);
		piglinPathRenderers.put(id, newRenderer);
		pathfindingRenderGroup.add(newRenderer);
	}

	private static void removePath(int id) {
		PathRenderer prevRenderer = piglinPathRenderers.remove(id);
		if (prevRenderer == null) return;
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
}
