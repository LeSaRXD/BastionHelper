package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.client.handler.PausePiglinsHandler;
import net.minecraft.client.render.entity.PiglinEntityRenderer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PiglinEntityRenderer.class)
public abstract class PiglinEntityRendererMixin extends LivingEntityRendererMixin {
	@Override
	protected float onRender(float oldTickDelta) {
		return PausePiglinsHandler.isPaused() ? 0.0f : oldTickDelta;
	}
}
