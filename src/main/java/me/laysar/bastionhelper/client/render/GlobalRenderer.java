package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.laysar.bastionhelper.client.handler.ShowLavaDeadzonesHandler;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import net.minecraft.client.util.math.MatrixStack;

public class GlobalRenderer extends Renderer {

	public static final GlobalRenderer INSTANCE = new GlobalRenderer();

	private MatrixStack matrixStack = null;

	public void setMatrixStack(MatrixStack value) {
		matrixStack = value;
	}

	public void clearMatrixStack() {
		matrixStack = null;
	}

	@Override
	public void render() {
		if (matrixStack == null) {
			return;
		}

		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(matrixStack.peek().getModel());
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();

		ShowPiglinPathsHandler.render();
		ShowLavaDeadzonesHandler.render();

		RenderSystem.popMatrix();
	}
}
