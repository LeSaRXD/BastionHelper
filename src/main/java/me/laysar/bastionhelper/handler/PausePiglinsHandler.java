package me.laysar.bastionhelper.handler;

public class PausePiglinsHandler {
	private static boolean paused = false;
	public static boolean isPaused() {
		return paused;
	}

	public static void pause() {
		paused = true;
	}
	public static void unpause() {
		paused = false;
	}
	public static void togglePause() {
		paused = !paused;
	}
}
