package me.laysar.bastionhelper.client;

import me.laysar.bastionhelper.client.keyboard.KeyInputManager;
import me.laysar.bastionhelper.client.network.ClientEventReceiver;
import net.fabricmc.api.ClientModInitializer;

public class BastionHelperClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerAll();
	}

	private void registerAll() {
		KeyInputManager.register();
		ClientEventReceiver.register();
	}
}
