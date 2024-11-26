package me.laysar.bastionhelper.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class Renderer {
	protected static final float LINE_WIDTH = 2.5f;
	protected static final int DRAW_MODE = 1;

	public abstract void render();

	protected Vec3d blockToVec(@NotNull BlockPos pos) {
		return new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
	}

	protected void addVertex(@NotNull BufferBuilder buffer, @NotNull Vec3d pos, @NotNull Color color) {
		Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
		float[] comps = color.getComponents(null);
		buffer.vertex(
				pos.x - cam.x, pos.y - cam.y, pos.z - cam.z
		).color(
				comps[0], comps[1], comps[2], comps[3]
		).next();
	}
}
