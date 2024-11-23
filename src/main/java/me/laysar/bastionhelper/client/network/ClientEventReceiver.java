package me.laysar.bastionhelper.client.network;

import me.laysar.bastionhelper.client.handler.AggroLevelsHandler;
import me.laysar.bastionhelper.client.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.client.handler.PausePiglinsHandler;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ClientEventReceiver {
	public static void register() {
		ClientSidePacketRegistry.INSTANCE.register(HIGHLIGHT_PIGLINS, HighlightPiglinsHandler::highlight);
		ClientSidePacketRegistry.INSTANCE.register(LOWLIGHT_PIGLINS, HighlightPiglinsHandler::lowlight);
		ClientSidePacketRegistry.INSTANCE.register(CREATE_PIGLIN_PATH, ShowPiglinPathsHandler::create);
		ClientSidePacketRegistry.INSTANCE.register(UPDATE_PIGLIN_PATH, ShowPiglinPathsHandler::update);
		ClientSidePacketRegistry.INSTANCE.register(PAUSE_PIGLINS, PausePiglinsHandler::pause);
		ClientSidePacketRegistry.INSTANCE.register(UNPAUSE_PIGLINS, PausePiglinsHandler::unpause);
		ClientSidePacketRegistry.INSTANCE.register(UPDATE_AGGRO_LEVEL, AggroLevelsHandler::update);
		ClientSidePacketRegistry.INSTANCE.register(REMOVE_AGGRO_LEVEL, AggroLevelsHandler::remove);
	}
}
