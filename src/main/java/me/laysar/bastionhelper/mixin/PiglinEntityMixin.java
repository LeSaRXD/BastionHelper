package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.handler.AggroLevelsHandler;
import me.laysar.bastionhelper.handler.PausePiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends LivingEntityMixin {
	@Override
	protected void afterSetHealth(float health, CallbackInfo ci) {
		PiglinEntity piglin = (PiglinEntity) (Object) this;
		if (piglin.world.isClient) {
			return;
		}

		if (health > 0.0f) {
			return;
		}

		AggroLevelsHandler.remove(piglin);
		ShowPiglinPathsHandler.remove(piglin.getEntityId());
	}

	@Override
	protected void onTick(CallbackInfo ci) {
		updateAggroLevel();
		pause(ci);
	}

	@Unique
	private void updateAggroLevel() {
		PiglinEntity piglin = (PiglinEntity) (Object) this;
		if (piglin.world.isClient) {
			return;
		}
		if (piglin.isBaby()) {
			return;
		}

		AggroLevelsHandler.update(piglin);
	}

	@Unique
	private void pause(CallbackInfo ci) {
		if (!PausePiglinsHandler.isPaused()) {
			return;
		}

		ci.cancel();

		PiglinEntity piglin = (PiglinEntity) (Object) this;
		if (piglin.world.isClient) {
			return;
		}
		piglin.getNavigation().tick();
	}
}
