package me.laysar.bastionhelper.handler;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HighlightPiglinsHandler {
	private static List<PiglinEntity> highlightedPiglins = null;

	public static void run(@NotNull PlayerEntity player) {
		if (highlightedPiglins == null) {
			highlightedPiglins = new ArrayList<>();
			StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.GLOWING, Integer.MAX_VALUE, 0, true, false);
			for (PiglinEntity piglin : player.world.getEntities(PiglinEntity.class, player.getBoundingBox().expand(100, 100, 100), (a) -> true)) {
				highlightedPiglins.add(piglin);
				piglin.applyStatusEffect(effect);
			}
		} else {
			for (PiglinEntity piglin : highlightedPiglins) {
				piglin.removeStatusEffect(StatusEffects.GLOWING);
			}
			highlightedPiglins = null;
		}
	}
}
