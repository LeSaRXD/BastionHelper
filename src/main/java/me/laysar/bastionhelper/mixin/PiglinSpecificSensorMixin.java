package me.laysar.bastionhelper.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import net.minecraft.entity.ai.brain.sensor.PiglinSpecificSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinSpecificSensor.class)
public abstract class PiglinSpecificSensorMixin {
	@ModifyExpressionValue(
			method = "sense(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V",
			at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z", ordinal = 0)
	)
	private boolean gameModeAndDifficultyTest(boolean original) {
		return CreativeFollowHandler.isFollow() || original;
	}
}
