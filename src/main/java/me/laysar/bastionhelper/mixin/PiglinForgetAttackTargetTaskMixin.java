package me.laysar.bastionhelper.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ForgetAttackTargetTask.class)
public class PiglinForgetAttackTargetTaskMixin {
	@Unique
	private boolean isPiglinTask = false;

	@Inject(method = "<init>(Ljava/util/function/Predicate;)V", at = @At("TAIL"))
	private void onInit(Predicate<?> alternativeCondition, CallbackInfo ci) {
		isPiglinTask = true;
	}

	@ModifyExpressionValue(method = "run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V",
	at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z", ordinal = 0))
	private boolean gameModeAndDifficultyTest(boolean original) {
		if (!isPiglinTask) return original;

		return CreativeFollowHandler.isFollow() || original;
	}
}
