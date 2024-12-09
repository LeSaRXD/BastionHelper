package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.handler.AggroLevelsHandler;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"))
	void afterTick(BooleanSupplier _supplier, CallbackInfo ci) {
		AggroLevelsHandler.afterTick((MinecraftServer) (Object) this);
	}
}
