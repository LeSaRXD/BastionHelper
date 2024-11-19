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
	private int currentNodeIndex;
	private Color color;

	public void setCurrentNodeIndex(int value) {
		currentNodeIndex = value;
	}
	public void setColor(Color value) {
		color = value;
	}

	public PathRenderer(@NotNull LivingEntity entity, @NotNull BlockPos[] positions, int currentNodeIndex, @NotNull Color color) {
		this.entity = entity;
		this.positions = positions;
		this.color = color;
		this.currentNodeIndex = currentNodeIndex;
	}

	@Override
	public void render() {
		BlockPos firstPos;
		try {
			firstPos = positions[currentNodeIndex + 1];
		} catch (IndexOutOfBoundsException _e) {
			return;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		GlStateManager.lineWidth(LINE_WIDTH);
		buffer.begin(DRAW_MODE, VertexFormats.POSITION_COLOR);

		this.addVertex(buffer, entity.getPos().add(0, 0.5, 0), color);
		this.addVertex(buffer, blockToVec(firstPos), color);

		for (int i = currentNodeIndex + 1; i < positions.length - 1; i++) {
			BlockPos from = positions[i],
					to = positions[i + 1];
			this.addVertex(buffer, blockToVec(from), color);
			this.addVertex(buffer, blockToVec(to), color);
		}
		tessellator.draw();
	}
}
