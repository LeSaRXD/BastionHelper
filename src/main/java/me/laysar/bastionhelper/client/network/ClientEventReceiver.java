package me.laysar.bastionhelper.client.network;

import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ClientEventReceiver {
	public static void register() {
		ClientSidePacketRegistry.INSTANCE.register(UPDATE_PIGLIN_PATH, ShowPiglinPathsHandler::update);
	}
}
