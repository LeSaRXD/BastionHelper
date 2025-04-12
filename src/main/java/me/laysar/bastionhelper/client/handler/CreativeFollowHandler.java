package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public class CreativeFollowHandler {
	private static boolean follow = false;

	public static void run() {
		if (follow) {
			ClientEventEmitter.creativeUnfollow();
		} else {
			ClientEventEmitter.creativeFollow();
		}
	}

	public static void follow(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		follow = true;
	}

	public static void unfollow(@NotNull PacketContext _ctx, @NotNull PacketByteBuf _buf) {
		follow = false;
	}
}
