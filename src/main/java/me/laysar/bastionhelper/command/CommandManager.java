package me.laysar.bastionhelper.command;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class CommandManager {
	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register(HighlightPiglinsCommand::register);
	}
}
