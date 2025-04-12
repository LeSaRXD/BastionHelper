package me.laysar.bastionhelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.laysar.bastionhelper.BastionHelper;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class LiteralCommand {
	private final String name;
	private final Command<ServerCommandSource> execute;
	public LiteralCommand(@NotNull String name, @NotNull Command<ServerCommandSource> execute) {
		this.name = name;
		this.execute = execute;

		CommandRegistrationCallback.EVENT.register((dispatcher, _d) -> this.register(dispatcher));
	}

	void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> cmd = CommandManager.literal(name).executes(ctx -> {
			try {
				return this.execute.run(ctx);
			} catch (Exception e) {
				BastionHelper.LOGGER.error(e);
				return 0;
			}
		});
		dispatcher.register(cmd);
	}
}
