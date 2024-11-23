package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.Helpers;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler.PiglinAggroLevel;
import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AggroLevelsHandler {
	public static PiglinAggroLevel updateAggroLevel(@NotNull PiglinEntity piglin, PiglinAggroLevel prevAggroLevel) {
		PiglinAggroLevel aggroLevel = Helpers.piglinAggroLevel(piglin);

		MinecraftServer server = piglin.getServer();
		if (server == null) return aggroLevel;

		if (aggroLevel == prevAggroLevel) return aggroLevel;

		for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (aggroLevel == PiglinAggroLevel.NONE)
				ServerEventEmitter.removeAggroLevel(player, piglin.getEntityId());
			else
				ServerEventEmitter.updateAggroLevel(player, piglin.getEntityId(), aggroLevel.toInt());
		}
		return aggroLevel;
	}
}
