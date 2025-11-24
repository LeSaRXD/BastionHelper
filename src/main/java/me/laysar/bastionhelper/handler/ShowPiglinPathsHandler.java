package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShowPiglinPathsHandler {
	private static final Map<Integer, Integer> sentNodeIndexes = new HashMap<>();

	public static void run(@NotNull PlayerEntity player) {
		ServerEventEmitter.togglePaths(player);
	}

	public static void create(int id, @Nullable Path path, List<ServerPlayerEntity> players) {
		if (PausePiglinsHandler.isPaused()) {
			return;
		}

		if (players.isEmpty()) {
			return;
		}
		if (path == null) {
			return;
		}

		for (PlayerEntity subscribedPlayer : players)
			ServerEventEmitter.createPiglinPath(subscribedPlayer, id, path);
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void update(int id, @Nullable Path path, List<ServerPlayerEntity> players) {
		if (PausePiglinsHandler.isPaused()) {
			return;
		}

		if (players.isEmpty() || path == null) {
			return;
		}

		Integer sentNodeIndex = sentNodeIndexes.get(id);
		if (sentNodeIndex == null) {
			create(id, path, players);
		} else if (sentNodeIndex == path.getCurrentNodeIndex()) {
			return;
		}

		for (PlayerEntity subscribedPlayer : players)
			ServerEventEmitter.updatePiglinPath(subscribedPlayer, id, path.getCurrentNodeIndex());
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void remove(int id, List<ServerPlayerEntity> players) {
		if (PausePiglinsHandler.isPaused()) {
			return;
		}

		if (players.isEmpty()) {
			return;
		}

		for (PlayerEntity subscribedPlayer : players)
			ServerEventEmitter.removePiglinPath(subscribedPlayer, id);
		sentNodeIndexes.remove(id);
	}
}
