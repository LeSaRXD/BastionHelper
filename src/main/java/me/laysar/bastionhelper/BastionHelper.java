package me.laysar.bastionhelper;

import me.laysar.bastionhelper.command.CommandManager;
import me.laysar.bastionhelper.network.ServerEventReceiver;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BastionHelper implements ModInitializer {

	public static final String MOD_ID = "bastionhelper";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		LOGGER.info("Bastion helper initialized!");

		registerAll();
	}

	private void registerAll() {
		ServerEventReceiver.register();
		CommandManager.register();
	}

}
