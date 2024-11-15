package me.laysar.bastionhelper.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.laysar.bastionhelper.client.handler.KeyInputHandler;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import me.laysar.bastionhelper.client.network.ClientEventReceiver;
import me.laysar.bastionhelper.client.render.RenderQueue;
import net.fabricmc.api.ClientModInitializer;

public class BastionHelperClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerAll();

		RenderQueue.get().add(matrixStack -> {
			RenderSystem.pushMatrix();
			RenderSystem.multMatrix(matrixStack.peek().getModel());
			GlStateManager.disableTexture();
			GlStateManager.disableDepthTest();

			if (ShowPiglinPathsHandler.showPaths) ShowPiglinPathsHandler.render();

			RenderSystem.popMatrix();
		});
	}

	private void registerAll() {
		KeyInputHandler.register();
		ClientEventReceiver.register();
	}

}
