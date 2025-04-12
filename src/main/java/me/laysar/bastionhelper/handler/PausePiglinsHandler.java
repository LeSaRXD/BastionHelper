package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PausePiglinsHandler {

	private static boolean paused = false;

	public static boolean isPaused() {
		return paused;
	}

	public static void run(@NotNull ServerPlayerEntity player) {
		paused = !paused;
		if (paused) {
			ServerEventEmitter.confirmPause(player);
		} else {
			ServerEventEmitter.confirmUnpause(player);
		}
	}

	public static void pause(@NotNull PacketContext ctx, @NotNull PacketByteBuf _buf) {
		if (paused) {
			return;
		}
		paused = true;
		ServerEventEmitter.confirmPause(ctx.getPlayer());
	}

	public static void unpause(@NotNull PacketContext ctx, @NotNull PacketByteBuf _buf) {
		if (!paused) {
			return;
		}
		paused = false;
		ServerEventEmitter.confirmUnpause(ctx.getPlayer());
	}
}
