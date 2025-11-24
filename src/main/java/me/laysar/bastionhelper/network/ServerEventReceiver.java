package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.handler.BabyPiglinGrowUpHandler;
import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import me.laysar.bastionhelper.handler.PausePiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ServerEventReceiver {
	public static void register() {
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.PAUSE_PIGLINS, PausePiglinsHandler::pause);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.UNPAUSE_PIGLINS, PausePiglinsHandler::unpause);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.CREATIVE_FOLLOW, CreativeFollowHandler::follow);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.CREATIVE_UNFOLLOW, CreativeFollowHandler::unfollow);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.GROW_UP_PIGLINS, BabyPiglinGrowUpHandler::execute);
	}
}
