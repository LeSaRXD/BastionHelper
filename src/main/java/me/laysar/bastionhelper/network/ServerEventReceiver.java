package me.laysar.bastionhelper.network;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ServerEventReceiver {
	public static void register() {
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.HIGHLIGHT_PIGLINS,
				(ctx, _data) -> OnHighlightPiglins(ctx.getPlayer()));
	}

	private static void OnHighlightPiglins(@NotNull PlayerEntity player) {
		StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.GLOWING, Integer.MAX_VALUE, 0, true, false);
		for (LivingEntity le : player.world.getEntities(EntityType.PIGLIN, player.getBoundingBox().expand(100, 100, 100), (a) -> true)) {
			le.applyStatusEffect(effect);
		}
	}
}
