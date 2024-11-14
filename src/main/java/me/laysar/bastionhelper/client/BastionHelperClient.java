package me.laysar.bastionhelper.client;

import me.laysar.bastionhelper.client.handler.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class BastionHelperClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KeyInputHandler.register();
	}

}
