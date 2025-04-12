package me.laysar.bastionhelper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityNavigation.class)
public abstract class PiglinNavigationMixin {

	@Shadow
	@Final
	protected MobEntity entity;

	@Shadow
	@Nullable
	public abstract Path getCurrentPath();

	@Shadow
	@Nullable
	protected Path currentPath;
	@Unique
	private static final long REMOVE_IN = 3L;
	@Unique
	private final MutableLong ticksUntilRemoved = new MutableLong(-1L);

	@Inject(method = "tick()V", at = @At("HEAD"))
	void onTick(CallbackInfo ci) {
		if (this.entity.world.isClient) {
			return;
		}
		if (!(this.entity instanceof PiglinEntity piglin)) {
			return;
		}
		if (!piglin.isAdult()) {
			return;
		}

		long untilRemoved = ticksUntilRemoved.getValue();
		if (untilRemoved < 0) {
			return;
		}

		if (ticksUntilRemoved.decrementAndGet() > 0) {
			return;
		}

		ShowPiglinPathsHandler.remove(piglin.getEntityId());
	}

	@ModifyReturnValue(method = "findPathToAny(Ljava/util/Set;IZI)Lnet/minecraft/entity/ai/pathing/Path;", at = @At("RETURN"))
	Path onFindPathToAny(Path original) {
		if (this.entity.world.isClient) {
			return original;
		}
		if (!(this.entity instanceof PiglinEntity piglin)) {
			return original;
		}
		if (!piglin.isAdult()) {
			return original;
		}

		ticksUntilRemoved.setValue(-1L);

		ShowPiglinPathsHandler.create(piglin.getEntityId(), original);
		return original;
	}

	@Inject(method = "adjustPath()V", at = @At("TAIL"))
	void afterAdjustPath(CallbackInfo ci) {
		if (this.entity.world.isClient) {
			return;
		}
		if (!(this.entity instanceof PiglinEntity piglin)) {
			return;
		}
		if (!piglin.isAdult()) {
			return;
		}
		if (this.currentPath == null) {
			return;
		}

		ShowPiglinPathsHandler.create(piglin.getEntityId(), this.currentPath);
	}

	@Inject(method = "*",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/Path;setCurrentNodeIndex(I)V"))
	void onSetCurrentNodeIndex(CallbackInfo ci) {
		if (this.entity.world.isClient) {
			return;
		}
		if (!(this.entity instanceof PiglinEntity piglin)) {
			return;
		}
		if (!piglin.isAdult()) {
			return;
		}

		ShowPiglinPathsHandler.update(piglin.getEntityId(), this.getCurrentPath());
	}

	@Inject(method = "stop()V", at = @At("HEAD"))
	void onRemovePath(CallbackInfo ci) {
		if (this.entity.world.isClient) {
			return;
		}
		if (!(this.entity instanceof PiglinEntity piglin)) {
			return;
		}
		if (!piglin.isAdult()) {
			return;
		}

		ticksUntilRemoved.setValue(REMOVE_IN);
	}
}
