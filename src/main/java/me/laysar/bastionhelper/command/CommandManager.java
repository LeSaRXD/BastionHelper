package me.laysar.bastionhelper.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.laysar.bastionhelper.handler.BabyPiglinGrowUpHandler;
import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import me.laysar.bastionhelper.handler.PausePiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import me.laysar.bastionhelper.network.ServerEventEmitter;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CommandManager {
	public static void register() {
		new LiteralCommand("highlight", CommandManager::highlightPiglins);
		new LiteralCommand("pause", CommandManager::pausePiglins);
		new LiteralCommand("paths", CommandManager::showPaths);
		new LiteralCommand("follow", CommandManager::creativeFollow);
		new LiteralCommand("growup", CommandManager::growUp);
	}

	private static int highlightPiglins(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerEventEmitter.toggleHighlights(ctx.getSource().getPlayer());
		return SINGLE_SUCCESS;
	}
	private static int pausePiglins(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		PausePiglinsHandler.run(ctx.getSource().getPlayer());
		return SINGLE_SUCCESS;
	}
	private static int showPaths(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ShowPiglinPathsHandler.run(ctx.getSource().getPlayer());
		return SINGLE_SUCCESS;
	}
	private static int creativeFollow(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		CreativeFollowHandler.run(ctx.getSource().getPlayer());
		return SINGLE_SUCCESS;
	}
	private static int growUp(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		BabyPiglinGrowUpHandler.run(ctx.getSource().getPlayer());
		return SINGLE_SUCCESS;
	}
}
