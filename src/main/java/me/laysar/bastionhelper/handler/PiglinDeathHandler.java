package me.laysar.bastionhelper.handler;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.NotNull;

public class PiglinDeathHandler {
	public static boolean enabled = false;

	public static void toggle() {
		enabled = !enabled;
	}

	public static void run(@NotNull PiglinEntity piglin, @NotNull ServerWorld world) {
		if (!enabled) return;

		for (ServerPlayerEntity player : world.getPlayers()) {
			player.sendMessage(new LiteralText("Piglin died @ ").append(piglin.getBlockPos().toShortString()), false);
		}
	}
}
