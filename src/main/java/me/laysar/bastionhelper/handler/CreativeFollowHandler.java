package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class CreativeFollowHandler {

	private static boolean follow = false;

	public static boolean isFollow() {
		return follow;
	}

	public static void run(@NotNull ServerPlayerEntity player) {
		follow = !follow;
		if (follow) {
			ServerEventEmitter.confirmFollow(player);
		} else {
			ServerEventEmitter.confirmUnfollow(player);
		}
	}



	public static void follow(@NotNull PacketContext ctx, @NotNull PacketByteBuf _buf) {
		if (follow) {
			return;
		}
		follow = true;
		ServerEventEmitter.confirmFollow(ctx.getPlayer());
	}

	public static void unfollow(@NotNull PacketContext ctx, @NotNull PacketByteBuf _buf) {
		if (!follow) {
			return;
		}
		follow = false;
		ServerEventEmitter.confirmUnfollow(ctx.getPlayer());
	}
}
