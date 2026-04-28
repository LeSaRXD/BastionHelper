package me.laysar.bastionhelper.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.laysar.bastionhelper.BastionHelper;
import net.minecraft.client.network.ClientPlayNetworkHandler;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onGameJoin", at = @At("TAIL"))
	private void onConnect(CallbackInfo ci) {
		BastionHelper.config.reapply();
	}
}
