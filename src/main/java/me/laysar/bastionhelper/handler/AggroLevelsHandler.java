package me.laysar.bastionhelper.handler;

import com.google.common.collect.ImmutableList;
import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AggroLevelsHandler {
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
				if (level.value == value) {
					return level;
				}
			}
			return NONE;
		}

		PiglinAggroLevel(int value) {
			this.value = value;
		}
	}

	private static Set<Integer> totalPiglins = Collections.synchronizedSet(new HashSet<>());
	private static Set<Integer> tickPiglins = Collections.synchronizedSet(new HashSet<>());

	private static Collection<ServerPlayerEntity> getServerPlayers(@NotNull Entity piglin) {
		MinecraftServer server = piglin.getServer();
		if (server == null) {
			return ImmutableList.of();
		}
		return server.getPlayerManager().getPlayerList();
	}

	public static void update(@NotNull PiglinEntity piglin) {
		int id = piglin.getEntityId();
		tickPiglins.add(id);

		PiglinAggroLevel aggroLevel = piglinAggroLevel(piglin);

		for (ServerPlayerEntity player : getServerPlayers(piglin))
			ServerEventEmitter.updateAggroLevel(player, id, aggroLevel);
	}

	public static void remove(@NotNull PiglinEntity piglin) {
		int id = piglin.getEntityId();
		tickPiglins.remove(id);

		for (ServerPlayerEntity player : getServerPlayers(piglin))
			ServerEventEmitter.removeAggroLevel(player, id);
	}

	public static void afterTick(@NotNull MinecraftServer server) {
		totalPiglins.removeAll(tickPiglins);
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
			for (int id : totalPiglins)
				ServerEventEmitter.removeAggroLevel(player, id);

		Set<Integer> temp = totalPiglins;
		totalPiglins = tickPiglins;
		tickPiglins = temp;
		tickPiglins.clear();
	}

	public static PiglinAggroLevel piglinAggroLevel(@NotNull PiglinEntity piglin) {
		Brain<PiglinEntity> brain = piglin.getBrain();

		if (brain.hasMemoryModule(MemoryModuleType.ADMIRING_ITEM)) return PiglinAggroLevel.GOLD_DISTRACTED;

		boolean lightAnger, mediumAnger = false, heavyAnger = false, goldDistracted;

		lightAnger = brain.hasMemoryModule(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);

		Optional<UUID> playerUuid = brain.getOptionalMemory(MemoryModuleType.ANGRY_AT);
		if (playerUuid.isPresent()) {
			ServerPlayerEntity player = (ServerPlayerEntity) piglin.getEntityWorld().getPlayerByUuid(playerUuid.get());
			if (player != null && player.interactionManager.getGameMode().isSurvivalLike()) {
				mediumAnger = true;
				heavyAnger = brain.getOptionalMemory(MemoryModuleType.ADMIRING_DISABLED).orElse(false);
			}
		}

		goldDistracted = brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);

		if (heavyAnger) return PiglinAggroLevel.HEAVY;
		if (goldDistracted) return PiglinAggroLevel.GOLD_DISTRACTED;
		if (mediumAnger) return PiglinAggroLevel.MEDIUM;
		if (lightAnger) return PiglinAggroLevel.LIGHT;
		return PiglinAggroLevel.NONE;
	}
}
