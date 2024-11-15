package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShowPiglinPathsHandler {
	private static final Map<Integer, Integer> sentNodeIndexes = new HashMap<>();
	private static PlayerEntity subscribedPlayer = null;

	public static void run(@NotNull PlayerEntity player) {
		if (subscribedPlayer == null) {
			subscribedPlayer = player;
		} else {
			subscribedPlayer = null;
			sentNodeIndexes.clear();
		}
	}

	public static void newPath(int id, @Nullable Path path) {
		if (subscribedPlayer == null) return;
		if (path == null) return;

		ServerEventEmitter.updatePiglinPath(subscribedPlayer, id, path);
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void updatePath(int id, @Nullable Path path) {
		if (subscribedPlayer == null) return;
		if (path == null) return;

		Integer sentNodeIndex = sentNodeIndexes.get(id);
		if (sentNodeIndex != null && sentNodeIndex == path.getCurrentNodeIndex()) return;

		ServerEventEmitter.updatePiglinPath(subscribedPlayer, id, path);
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void removePath(int id) {
		if (subscribedPlayer == null) return;

		ServerEventEmitter.removePiglinPath(subscribedPlayer, id);
		sentNodeIndexes.remove(id);
	}

	public static void clear() {
		sentNodeIndexes.clear();
		subscribedPlayer = null;
	}
}
