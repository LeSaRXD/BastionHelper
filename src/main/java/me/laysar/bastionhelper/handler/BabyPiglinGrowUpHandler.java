package me.laysar.bastionhelper.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class BabyPiglinGrowUpHandler {
	public static void run(@NotNull ServerPlayerEntity player) {
		for (Entity entity : player.getServerWorld().getEntities(EntityType.PIGLIN, _p -> true)) {
			PiglinEntity piglin = (PiglinEntity) entity;
			if (piglin.isBaby()) {
				piglin.setBaby(false);
			}
		}
	}
}
