package me.laysar.bastionhelper.command;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class CommandManager {
	public static void register() {
		CommandRegistrationCallback.EVENT.register(HighlightPiglinsCommand::register);
		CommandRegistrationCallback.EVENT.register(ShowPiglinPathsCommand::register);
		CommandRegistrationCallback.EVENT.register(PausePiglinsCommand::register);
	}
}
