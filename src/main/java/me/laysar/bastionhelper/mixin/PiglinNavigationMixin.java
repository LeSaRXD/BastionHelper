package me.laysar.bastionhelper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler.PiglinAggroLevel;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(EntityNavigation.class)
public abstract class PiglinNavigationMixin {
	@Shadow @Final protected MobEntity entity;
	@Shadow @Nullable public abstract Path getCurrentPath();

	@Shadow protected int tickCount;
	@Shadow @Nullable protected Path currentPath;
	@Unique
	private static final long bastionhelper$REMOVE_IN = 3L;
	@Unique
	private final MutableLong bastionhelper$ticksUntilRemoved = new MutableLong(-1L);

	@Inject(method = "tick()V", at = @At("HEAD"))
	void tick(CallbackInfo ci) {
		if (this.entity.world.isClient) return;
		if (!(this.entity instanceof PiglinEntity piglin)) return;

		long ticksUntilRemoved = bastionhelper$ticksUntilRemoved.getValue();
		if (ticksUntilRemoved < 0) return;

		if (bastionhelper$ticksUntilRemoved.decrementAndGet() > 0) return;

		ShowPiglinPathsHandler.remove(piglin.getEntityId());
	}

	@ModifyReturnValue(method = "findPathToAny(Ljava/util/Set;IZI)Lnet/minecraft/entity/ai/pathing/Path;", at = @At("RETURN"))
	Path onCreatePath(Path original) {
		if (this.entity.world.isClient) return original;
		if (!(this.entity instanceof PiglinEntity piglin)) return original;

		bastionhelper$ticksUntilRemoved.setValue(-1L);

		ShowPiglinPathsHandler.create(piglin.getEntityId(), original, bastionhelper$aggroLevel(piglin));
		return original;
	}

	@Inject(method = "adjustPath()V", at = @At("TAIL"))
	void onAdjustPath(CallbackInfo ci) {
		if (this.entity.world.isClient) return;
		if (this.currentPath == null) return;
		if (!(this.entity instanceof PiglinEntity piglin)) return;

		ShowPiglinPathsHandler.create(piglin.getEntityId(), this.currentPath, bastionhelper$aggroLevel(piglin));
	}

	@Inject(method = "*",
	at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/Path;setCurrentNodeIndex(I)V"))
	void onSetCurrentNodeIndex(CallbackInfo ci) {
		if (this.entity.world.isClient) return;
		if (!(this.entity instanceof PiglinEntity piglin)) return;

		ShowPiglinPathsHandler.update(piglin.getEntityId(), this.getCurrentPath(), bastionhelper$aggroLevel(piglin));
	}

	@Inject(method = "stop()V", at = @At("HEAD"))
	void onRemovePath(CallbackInfo ci) {
		if (this.entity.world.isClient) return;
		if (!(this.entity instanceof PiglinEntity piglin)) return;

		if (bastionhelper$aggroLevel(piglin) != PiglinAggroLevel.NONE)
			bastionhelper$ticksUntilRemoved.setValue(bastionhelper$REMOVE_IN);
		else
			ShowPiglinPathsHandler.remove(piglin.getEntityId());
	}

	@Unique
	private PiglinAggroLevel bastionhelper$aggroLevel(@NotNull PiglinEntity piglin) {
		boolean lightAnger, mediumAnger = false, heavyAnger = false, goldDistracted;

		Brain<PiglinEntity> brain = piglin.getBrain();
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
