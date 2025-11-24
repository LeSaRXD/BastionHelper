package me.laysar.bastionhelper.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.laysar.bastionhelper.BastionHelper;
import me.laysar.bastionhelper.client.handler.AggroLevelsHandler;
import me.laysar.bastionhelper.client.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Inject(method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;)V", at = @At("HEAD"))
	private void onConnect(CallbackInfo ci) {
		BastionHelper.config.reapply();
	}

	@Inject(method = "disconnect()V", at = @At("HEAD"))
	private void onDisconnect(CallbackInfo ci) {
		ShowPiglinPathsHandler.clear();
		AggroLevelsHandler.clear();
	}

	@ModifyReturnValue(method = "method_27022(Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"))
	private boolean onCheckGlowing(boolean original, @Local(ordinal = 0, argsOnly = true) Entity entity) {
		if (!(entity instanceof PiglinEntity piglin)) {
			return original;
		}
		if (piglin.isBaby()) {
			return original;
		}

		return HighlightPiglinsHandler.getHighlighted() || original;
	}
}
