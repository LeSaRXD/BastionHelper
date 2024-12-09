package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PausePiglinsHandler {

	private static boolean paused = false;

	public static boolean isPaused() {
		return paused;
	}

	public static void run(@NotNull PlayerEntity player) {
		paused = !paused;
		if (paused) {
			ServerEventEmitter.confirmPause(player);
		} else {
			ServerEventEmitter.confirmUnpause(player);
		}
	}

	public static void pause(@NotNull PlayerEntity player) {
		if (paused) {
			return;
		}
		paused = true;
		ServerEventEmitter.confirmPause(player);
	}

	public static void unpause(@NotNull PlayerEntity player) {
		if (!paused) {
			return;
		}
		paused = false;
		ServerEventEmitter.confirmUnpause(player);
	}
}
