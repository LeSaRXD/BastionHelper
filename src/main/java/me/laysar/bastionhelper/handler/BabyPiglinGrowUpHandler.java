package me.laysar.bastionhelper.handler;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class BabyPiglinGrowUpHandler {
	public static void run(@NotNull ServerPlayerEntity player) {
		for (Entity entity : player.getServerWorld().getEntities(EntityType.PIGLIN, e -> ((PiglinEntity) e).isBaby())) {
			PiglinEntity piglin = (PiglinEntity) entity;
			piglin.setBaby(false);
		}
	}

	public static void execute(@NotNull PacketContext ctx, @NotNull PacketByteBuf _buf) {
		if (ctx.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
			run(serverPlayer);
		}
	}
}
