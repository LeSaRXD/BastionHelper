package me.laysar.bastionhelper.handler;

import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ShowPiglinPathsHandler {
	public enum PiglinAggroLevel {
		NONE(0),
		LIGHT(1),
		MEDIUM(2),
		GOLD_DISTRACTED(3),
		HEAVY(4);

		private final int value;
		public int toInt() {
			return this.value;
		}
		public static PiglinAggroLevel fromInt(int value) {
			for (PiglinAggroLevel level : values()) {
				if (level.value == value)
					return level;
			}
			return NONE;
		}

		PiglinAggroLevel(int value) {
			this.value = value;
		}
	}

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

	public static void create(int id, @Nullable Path path, PiglinAggroLevel aggroLevel) {
		if (subscribedPlayer == null) return;
		if (path == null) return;

		ServerEventEmitter.createPiglinPath(subscribedPlayer, id, path, aggroLevel.toInt());
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void update(int id, @Nullable Path path, PiglinAggroLevel aggroLevel) {
		if (subscribedPlayer == null ||
				path == null) return;

		Integer sentNodeIndex = sentNodeIndexes.get(id);
		if (sentNodeIndex == null) create(id, path, aggroLevel);
		else if (sentNodeIndex == path.getCurrentNodeIndex()) return;

		ServerEventEmitter.updatePiglinPath(subscribedPlayer, id, path.getCurrentNodeIndex(), aggroLevel.toInt());
		sentNodeIndexes.put(id, path.getCurrentNodeIndex());
	}

	public static void remove(int id) {
		if (subscribedPlayer == null) return;

		ServerEventEmitter.removePiglinPath(subscribedPlayer, id);
		sentNodeIndexes.remove(id);
	}

	public static void clear() {
		sentNodeIndexes.clear();
		subscribedPlayer = null;
	}
}
