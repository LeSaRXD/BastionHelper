package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL32;

import java.awt.*;

public class CuboidRenderer extends Renderer {
	protected static final int DRAW_MODE = 7;

	private final Vec3i position, size;
	private final Color color;

	public CuboidRenderer(@NotNull Vec3i pos, @NotNull Vec3i size, @NotNull Color color) {
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
		for (Direction dir : Direction.values())
			this.addFace(buffer, Vec3d.of(position), Vec3d.of(size), dir, color);

		tessellator.draw();

		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}
}
