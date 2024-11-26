package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL32;

import java.awt.*;

public class CuboidRenderer extends Renderer {
	protected static final int DRAW_MODE = 7;

	private final Vec3d position, size;
	private final Color color;

	public CuboidRenderer(@NotNull Vec3d pos, @NotNull Vec3d size, @NotNull Color color) {
		this.position = pos;
		this.size = size;
		this.color = color;
	}

	@Override
	public void render() {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		buffer.begin(DRAW_MODE, VertexFormats.POSITION_COLOR);
		for (FaceDir dir : FaceDir.values())
			this.addFace(buffer, position, size, dir, color);

		tessellator.draw();

		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	protected enum FaceDir {
		UP,
		DOWN,
		RIGHT,
		LEFT,
		FORWARD,
		BACKWARDS
	}
	protected void addFace(@NotNull BufferBuilder buffer, @NotNull Vec3d pos, @NotNull Vec3d size, @NotNull FaceDir dir, @NotNull Color color) {
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
			case FORWARD -> {
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
			case BACKWARDS -> {
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
			case LEFT -> {
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
			case RIGHT -> {
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
}
