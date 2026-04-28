package me.laysar.bastionhelper.client.handler;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public class HighlightPiglinsHandler {
	public static boolean highlighted = false;

	public static void run() {
		highlighted = !highlighted;
	}

	public static void toggle(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		run();
	}
}
