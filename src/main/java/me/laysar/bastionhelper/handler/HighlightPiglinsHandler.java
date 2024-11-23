package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class HighlightPiglinsHandler {
	private static boolean highlighted = false;
	public static boolean isHighlighted() {
		return highlighted;
	}

	public static void run(@NotNull PlayerEntity player) {
		highlighted = !highlighted;
		if (highlighted)
			ServerEventEmitter.confirmHighlight(player);
		else
			ServerEventEmitter.confirmLowlight(player);
	}
	public static void highlight(@NotNull PlayerEntity player) {
		if (highlighted) return;
		highlighted = true;
		ServerEventEmitter.confirmHighlight(player);
	}
	public static void lowlight(@NotNull PlayerEntity player) {
		if (!highlighted) return;
		highlighted = false;
		ServerEventEmitter.confirmLowlight(player);
	}
}
