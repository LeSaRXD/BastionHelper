package me.laysar.bastionhelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ShowPiglinPathsCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean _dispatched) {
		LiteralArgumentBuilder<ServerCommandSource> cmd = CommandManager.literal("pathfinding").executes(ShowPiglinPathsCommand::run);
		dispatcher.register(cmd);
	}

	public static int run(CommandContext<ServerCommandSource> _ctx) {
		try {
			ShowPiglinPathsHandler.run();
			return Command.SINGLE_SUCCESS;
		} catch (Exception e) {
			return 0;
		}
	}
}
