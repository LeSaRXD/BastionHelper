package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;

public class PiglinDeathHandler {
	public static void setEnabled(boolean value) {
		if (value)
			ClientEventEmitter.enableDeathMessages();
		else
			ClientEventEmitter.disableDeathMessages();
	}
}
