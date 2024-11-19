package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class BlockRenderer extends Renderer {
	private final Vec3d[] verts;
	private Color color;
	public void setColor(Color value) {
		color = value;
	}

	public BlockRenderer(BlockPos blockPos, Color color) {
		this.verts = new Vec3d[24];
		Vec3d center = blockToVec(blockPos);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				int idx = 2 * (2 * i + j);

				verts[idx     ] = center.add(i - 0.5, j - 0.5, -0.5);
				verts[idx +  1] = center.add(i - 0.5, j - 0.5, 0.5);
				verts[idx +  8] = center.add(i - 0.5, -0.5, j - 0.5);
				verts[idx +  9] = center.add(i - 0.5, 0.5, j - 0.5);
				verts[idx + 16] = center.add(-0.5, i - 0.5, j - 0.5);
				verts[idx + 17] = center.add(0.5, i - 0.5, j - 0.5);
			}
		}

		this.color = color;
	}

	@Override
	public void render() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		GlStateManager.lineWidth(LINE_WIDTH);
		buffer.begin(DRAW_MODE, VertexFormats.POSITION_COLOR);

		for (int i = 0; i < 24; i += 2) {
			this.addVertex(buffer, verts[i], color);
			this.addVertex(buffer, verts[i + 1], color);
		}

		tessellator.draw();
	}
}
