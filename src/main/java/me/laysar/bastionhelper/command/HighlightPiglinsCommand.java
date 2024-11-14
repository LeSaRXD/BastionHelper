package me.laysar.bastionhelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.laysar.bastionhelper.handler.HighlightPiglinsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class HighlightPiglinsCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dispatched) {
		LiteralArgumentBuilder<ServerCommandSource> cmd = CommandManager.literal("highlight").executes(HighlightPiglinsCommand::run);
		dispatcher.register(cmd);
	}

	public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		try {
			HighlightPiglinsHandler.run(ctx.getSource().getPlayer());
			return Command.SINGLE_SUCCESS;
		} catch (Exception e) {
			return 0;
		}
	}
}
