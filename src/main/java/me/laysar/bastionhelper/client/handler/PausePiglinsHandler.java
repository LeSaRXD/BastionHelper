package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;

public class PausePiglinsHandler {
	private static boolean paused = false;

	public static void run() {
		paused = !paused;
		if (paused) {
			ClientEventEmitter.pausePiglins();
		} else {
			ClientEventEmitter.unpausePiglins();
		}
	}
}
