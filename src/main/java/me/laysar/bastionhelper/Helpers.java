package me.laysar.bastionhelper;

import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler.PiglinAggroLevel;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class Helpers {
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
