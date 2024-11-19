package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends LivingEntityMixin {
	@Override
	protected void onDeath(float health, CallbackInfo ci) {
		if (health <= 0.0f)
			ShowPiglinPathsHandler.remove(((LivingEntity) (Object) this).getEntityId());
	}
}
