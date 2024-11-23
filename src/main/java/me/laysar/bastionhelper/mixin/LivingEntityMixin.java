package me.laysar.bastionhelper.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow public abstract boolean removeStatusEffect(StatusEffect type);

	@Inject(method = "setHealth(F)V", at = @At("TAIL"))
	protected void onDeath(float health, CallbackInfo ci) {}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	protected void onTick(CallbackInfo ci) {}
}
