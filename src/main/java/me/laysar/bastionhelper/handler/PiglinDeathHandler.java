package me.laysar.bastionhelper.handler;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;

public class PiglinDeathHandler {
	public static boolean enabled = false;

	public static void toggle() {
		enabled = !enabled;
	}

	public static void run(@NotNull PiglinEntity piglin, @NotNull ServerWorld world) {
		if (!enabled)
			return;

		for (ServerPlayerEntity player : world.getPlayers()) {
			BlockPos deathPos = piglin.getBlockPos();
			Text coordsText = Texts
					.bracketed(
							new TranslatableText("chat.coordinates",
									new Object[] { deathPos.getX(), deathPos.getY(), deathPos.getZ() }))
					.styled((style) -> style.withColor(Formatting.GREEN)
							.withClickEvent(
									new ClickEvent(Action.SUGGEST_COMMAND, toCommand(deathPos)))
							.setHoverEvent(new HoverEvent(net.minecraft.text.HoverEvent.Action.SHOW_TEXT,
									new TranslatableText("chat.coordinates.tooltip"))));
			player.sendMessage(new LiteralText("Piglin died @ ").append(coordsText), false);
		}
	}

	private static String toCommand(BlockPos pos) {
		return String.format("/tp @s %s %s %s", pos.getX(), pos.getY(), pos.getZ());
	}
}
