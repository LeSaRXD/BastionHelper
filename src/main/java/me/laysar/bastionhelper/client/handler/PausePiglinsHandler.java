package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;

public class PausePiglinsHandler {
	private static boolean paused = false;

	public static void run() {
		if (paused)
			ClientEventEmitter.unpausePiglins();
		else
			ClientEventEmitter.pausePiglins();
	}

	public static void pause(PacketContext _ctx, PacketByteBuf _buf) {
		paused = true;
	}
	public static void unpause(PacketContext _ctx, PacketByteBuf _buf) {
		paused = false;
	}
}
