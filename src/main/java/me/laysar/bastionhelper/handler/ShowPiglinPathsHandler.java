package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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

	public static void toggle(@NotNull PacketContext ctx, @NotNull PacketByteBuf buf) {
		run(ctx.getPlayer());
	}

	public static void create(int id, @Nullable Path path) {
		if (PausePiglinsHandler.isPaused()) {
			return;
		}

		if (subscribedPlayer == null) {
			return;
		}
		if (path == null) {
			return;
		}

		ServerEventEmitter.createPiglinPath(subscribedPlayer, id, path);
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void update(int id, @Nullable Path path) {
		if (PausePiglinsHandler.isPaused()) {
			return;
		}

		if (subscribedPlayer == null || path == null) {
			return;
		}

		Integer sentNodeIndex = sentNodeIndexes.get(id);
		if (sentNodeIndex == null) {
			create(id, path);
		} else if (sentNodeIndex == path.getCurrentNodeIndex()) {
			return;
		}

		ServerEventEmitter.updatePiglinPath(subscribedPlayer, id, path.getCurrentNodeIndex());
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void remove(int id) {
		if (PausePiglinsHandler.isPaused()) {
			return;
		}

		if (subscribedPlayer == null) {
			return;
		}

		ServerEventEmitter.removePiglinPath(subscribedPlayer, id);
		sentNodeIndexes.remove(id);
	}
}
