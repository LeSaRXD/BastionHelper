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
		highlighted = !highlighted;
	}

	public static void toggle(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		run();
	}
}
