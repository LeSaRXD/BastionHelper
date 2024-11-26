package me.laysar.bastionhelper.client;

import me.laysar.bastionhelper.client.handler.KeyInputHandler;
import me.laysar.bastionhelper.client.network.ClientEventReceiver;
import net.fabricmc.api.ClientModInitializer;

public class BastionHelperClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerAll();
	}

	private void registerAll() {
		KeyInputHandler.register();
		ClientEventReceiver.register();
	}
}
