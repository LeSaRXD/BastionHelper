package me.laysar.bastionhelper.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class Renderer {
	protected static final float LINE_WIDTH = 2.5f;
	protected static final int DRAW_MODE = 1;

	protected GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;

	public abstract void render();

	protected Vec3d blockToVec(@NotNull BlockPos pos) {
		return new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
	}

	protected void addVertex(@NotNull BufferBuilder buffer, @NotNull Vec3d pos, @NotNull Color color) {
		Vec3d cam = this.renderer.getCamera().getPos();
		float[] colorComp = color.getComponents(null);
		buffer.vertex(
				pos.x - cam.x, pos.y - cam.y, pos.z - cam.z
		).color(
				colorComp[0], colorComp[1], colorComp[2], colorComp[3]
		).next();
	}
}
