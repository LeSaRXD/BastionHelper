package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Inject(method = "disconnect()V", at = @At("HEAD"))
	void disconnect(CallbackInfo ci) {
		ShowPiglinPathsHandler.clear();
	}
}
