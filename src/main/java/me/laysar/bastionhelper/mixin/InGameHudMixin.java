package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.client.handler.AggroLevelsHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow public abstract TextRenderer getFontRenderer();

	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;

	@Shadow @Final private MinecraftClient client;

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("TAIL"))
	void onRender(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
		if (this.client.options.hudHidden) return;
		TextRenderer fontRenderer = this.getFontRenderer();
		float scaledWidth = this.client.getWindow().getScaledWidth(),
				scaledHeight = this.client.getWindow().getScaledHeight();

		float gap = 2f;
		float x = scaledWidth / 2.0f - 91f - 29f;

		float constWidth = fontRenderer.getWidth("  999");
		float constHeight = fontRenderer.fontHeight;

		int[] levelCounts = AggroLevelsHandler.aggroLevelCounts();
		Text goldDistractedText = new LiteralText(String.valueOf(levelCounts[2])),
				lightAggroText = new LiteralText(String.valueOf(levelCounts[0])),
				mediumAggroText = new LiteralText(String.valueOf(levelCounts[1])),
				heavyAggroText = new LiteralText(String.valueOf(levelCounts[3]));
		float goldDistractedWidth = fontRenderer.getWidth(goldDistractedText),
				lightAggroWidth = fontRenderer.getWidth(lightAggroText),
				mediumAggroWidth = fontRenderer.getWidth(mediumAggroText),
				heavyAggroWidth = fontRenderer.getWidth(heavyAggroText);

		DrawableHelper.fill(matrixStack,
				(int) (x - (constWidth + Math.max(goldDistractedWidth, lightAggroWidth) + 3 * gap)),
				(int) (scaledHeight - (2 * constHeight + 3 * gap)),
				(int) x,
				(int) scaledHeight,
				-1873784752);

		fontRenderer.drawWithShadow(matrixStack,
				goldDistractedText,
				x - constWidth - goldDistractedWidth - 2 * gap,
				scaledHeight - 2 * constHeight - 2 * gap,
				Color.GREEN.getRGB());
		fontRenderer.drawWithShadow(matrixStack,
				lightAggroText,
				x - constWidth - lightAggroWidth - 2 * gap,
				scaledHeight - constHeight - gap,
				Color.YELLOW.getRGB());
		fontRenderer.drawWithShadow(matrixStack,
				mediumAggroText,
				x - mediumAggroWidth - gap,
				scaledHeight - 2 * constHeight - 2 * gap,
				Color.ORANGE.getRGB());
		fontRenderer.drawWithShadow(matrixStack,
				heavyAggroText,
				x - heavyAggroWidth - gap,
				scaledHeight - constHeight - gap,
				Color.RED.getRGB());
	}
}
