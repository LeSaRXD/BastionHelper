package me.laysar.bastionhelper.client.network;

import me.laysar.bastionhelper.client.handler.*;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ClientEventReceiver {
	public static void register() {
		ClientSidePacketRegistry.INSTANCE.register(HIGHLIGHT_PIGLINS, HighlightPiglinsHandler::toggle);
		ClientSidePacketRegistry.INSTANCE.register(CREATE_PIGLIN_PATH, ShowPiglinPathsHandler::create);
		ClientSidePacketRegistry.INSTANCE.register(UPDATE_PIGLIN_PATH, ShowPiglinPathsHandler::update);
		ClientSidePacketRegistry.INSTANCE.register(REMOVE_PIGLIN_PATH, ShowPiglinPathsHandler::remove);
		ClientSidePacketRegistry.INSTANCE.register(PAUSE_PIGLINS, PausePiglinsHandler::pause);
		ClientSidePacketRegistry.INSTANCE.register(UNPAUSE_PIGLINS, PausePiglinsHandler::unpause);
		ClientSidePacketRegistry.INSTANCE.register(UPDATE_AGGRO_LEVEL, AggroLevelsHandler::update);
		ClientSidePacketRegistry.INSTANCE.register(REMOVE_AGGRO_LEVEL, AggroLevelsHandler::remove);
		ClientSidePacketRegistry.INSTANCE.register(CREATIVE_FOLLOW, CreativeFollowHandler::follow);
		ClientSidePacketRegistry.INSTANCE.register(CREATIVE_UNFOLLOW, CreativeFollowHandler::unfollow);
	}
}
