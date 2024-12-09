package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.render.MeshRenderer;
import me.laysar.bastionhelper.client.render.RenderGroup;
import me.laysar.bastionhelper.client.render.RenderGroup.RenderOption;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShowLavaDeadzonesHandler {

	private static final Vec3i ONE = new Vec3i(1, 1, 1);

	private static boolean enabled = false;
	private static final Set<Vec3i> lavaPositions = Collections.synchronizedSet(new HashSet<>());
	private static final RenderGroup<MeshRenderer> lavaRenderGroup = new RenderGroup<>(1, RenderOption.FRONT_NO_CULL);
	private static final MeshRenderer lavaMesh = new MeshRenderer(new Color(1.0f, 0.2f, 0.0f, 0.15f));

	static {
		lavaRenderGroup.add(lavaMesh);
	}

	public static void run() {
		enabled = !enabled;
	}

	public static void addLava(@NotNull BlockPos pos) {
		if (!lavaPositions.add(pos)) {
			return;
		}

		lavaMesh.addPositions(pos.subtract(ONE), pos.add(ONE));
	}

	public static void removeLava(@NotNull BlockPos pos) {
		if (!lavaPositions.remove(pos)) {
			return;
		}

		lavaMesh.removePositions(pos.subtract(ONE), pos.add(ONE));
	}

	public static void render() {
		if (!enabled) {
			return;
		}

		lavaRenderGroup.render();
	}

	public static void clear() {
		lavaPositions.clear();
		lavaMesh.clear();
	}
}
