package me.laysar.bastionhelper.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.laysar.bastionhelper.client.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.client.handler.AggroLevelsHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.Color;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@ModifyArgs(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V",
	at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"))
	private void onSetOutlineColor(Args args, @Local(ordinal = 0) Entity entity) {
		if (!HighlightPiglinsHandler.getHighlighted()) {
			return;
		}
		if (!(entity instanceof PiglinEntity piglin)) {
			return;
		}
		Color color = AggroLevelsHandler.getAggroLevel(piglin.getEntityId()).toColor();
		args.setAll(color.getRed(), color.getGreen(), color.getBlue(), 255);
	}
}
