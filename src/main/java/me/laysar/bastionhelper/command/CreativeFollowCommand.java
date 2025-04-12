package me.laysar.bastionhelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import me.laysar.bastionhelper.handler.PausePiglinsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class CreativeFollowCommand {
	public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher, boolean _dispatched) {
		LiteralArgumentBuilder<ServerCommandSource> cmd = CommandManager.literal("follow").executes(CreativeFollowCommand::run);
		dispatcher.register(cmd);
	}

	public static int run(CommandContext<ServerCommandSource> ctx) {
		try {
			CreativeFollowHandler.run(ctx.getSource().getPlayer());
			return Command.SINGLE_SUCCESS;
		} catch (Exception e) {
			return 0;
		}
	}
}
