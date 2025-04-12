package me.laysar.bastionhelper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
	@ModifyReturnValue(method = "shouldAttack(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"))
	private static boolean shouldAttack(boolean original) {
		return CreativeFollowHandler.isFollow() || original;
	}
}
