package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public class HighlightPiglinsHandler {
	private static boolean highlighted = false;

	public static boolean getHighlighted() {
		return highlighted;
	}

	public static void run() {
		if (highlighted) {
			ClientEventEmitter.lowlightPiglins();
		} else {
			ClientEventEmitter.highlightPiglins();
		}
	}

	public static void highlight(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		highlighted = true;
	}

	public static void lowlight(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		highlighted = false;
	}
}
