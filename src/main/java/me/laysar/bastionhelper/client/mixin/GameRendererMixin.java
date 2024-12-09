package me.laysar.bastionhelper.client.mixin;

import me.laysar.bastionhelper.client.render.GlobalRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "renderWorld", at = @At("HEAD"))
	private void onRenderWorld(float delta, long time, MatrixStack matrixStack, CallbackInfo ci) {
		GlobalRenderer.INSTANCE.setMatrixStack(matrixStack);
	}

	@Inject(method = "renderWorld", at = @At("TAIL"))
	private void afterRenderWorld(float delta, long time, MatrixStack matrixStack, CallbackInfo ci) {
		GlobalRenderer.INSTANCE.clearMatrixStack();
	}
}
