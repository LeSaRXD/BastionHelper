package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class CreativeFollowHandler {

	private static boolean follow = false;

	public static boolean isFollow() {
		return follow;
	}

	public static void run(@NotNull PlayerEntity player) {
		follow = !follow;
		if (follow) {
			ServerEventEmitter.confirmFollow(player);
		} else {
			ServerEventEmitter.confirmUnfollow(player);
		}
	}

	public static void follow(@NotNull PlayerEntity player) {
		if (follow) {
			return;
		}
		follow = true;
		ServerEventEmitter.confirmFollow(player);
	}

	public static void unfollow(@NotNull PlayerEntity player) {
		if (!follow) {
			return;
		}
		follow = false;
		ServerEventEmitter.confirmUnfollow(player);
	}
}
