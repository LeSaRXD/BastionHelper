package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PathRenderer extends Renderer {
	private final LivingEntity entity;
	private final BlockPos[] positions;
	private final Color color;
	private final BlockRenderer targetRenderer;

	public PathRenderer(@NotNull LivingEntity entity, @NotNull BlockPos[] positions, @NotNull Color color, @NotNull BlockPos target, @NotNull Color targetColor) {
		this.entity = entity;
		this.positions = positions;
		this.color = color;

		this.targetRenderer = new BlockRenderer(target, targetColor);
	}

	@Override
	public void render() {
		if (positions.length == 0) {
			targetRenderer.render();
			return;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		GlStateManager.lineWidth(LINE_WIDTH);
		buffer.begin(DRAW_MODE, VertexFormats.POSITION_COLOR);

		this.addVertex(buffer, entity.getPos().add(0, 0.5, 0), color);
		this.addVertex(buffer, blockToVec(positions[0]), color);

		for (int i = 0; i < positions.length - 1; i++) {
			BlockPos from = positions[i],
					to = positions[i + 1];
			this.addVertex(buffer, blockToVec(from), color);
			this.addVertex(buffer, blockToVec(to), color);
		}
		tessellator.draw();

		targetRenderer.render();
	}
}
