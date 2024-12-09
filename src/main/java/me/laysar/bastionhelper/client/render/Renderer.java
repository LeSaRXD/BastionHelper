package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL32;

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

	protected void addFace(@NotNull BufferBuilder buffer, @NotNull Vec3d pos, @NotNull Vec3d size, @NotNull Direction dir, @NotNull Color color) {
		Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
		pos = pos.subtract(cam);
		size = size.multiply(0.5);

		float[] comps = color.getComponents(null);

		switch (dir) {
			case UP -> {
				double up = pos.y + size.y;
				buffer.vertex(pos.x - size.x, up, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x - size.x, up, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, up, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, up, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
			}
			case DOWN -> {
				double down = pos.y - size.y;
				buffer.vertex(pos.x - size.x, down, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, down, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, down, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x - size.x, down, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
			}
			case NORTH -> {
				double front = pos.z - size.z;
				buffer.vertex(pos.x - size.x, pos.y - size.y, front)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x - size.x, pos.y + size.y, front)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, pos.y + size.y, front)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, pos.y - size.y, front)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
			}
			case SOUTH -> {
				double back = pos.z + size.z;
				buffer.vertex(pos.x - size.x, pos.y - size.y, back)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, pos.y - size.y, back)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x + size.x, pos.y + size.y, back)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(pos.x - size.x, pos.y + size.y, back)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
			}
			case WEST -> {
				double left = pos.x - size.x;
				buffer.vertex(left, pos.y - size.y, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(left, pos.y - size.y, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(left, pos.y + size.y, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(left, pos.y + size.y, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
			}
			case EAST -> {
				double right = pos.x + size.x;
				buffer.vertex(right, pos.y - size.y, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(right, pos.y + size.y, pos.z - size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(right, pos.y + size.y, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
				buffer.vertex(right, pos.y - size.y, pos.z + size.z)
						.color(comps[0], comps[1], comps[2], comps[3]).next();
			}
		}
	}

	protected void enableTransparency() {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);
	}

	protected void disableTransparency() {
		RenderSystem.disableBlend();
	}
}
