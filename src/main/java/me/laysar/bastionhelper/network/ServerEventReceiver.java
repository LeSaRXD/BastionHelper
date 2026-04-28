package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.handler.*;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ServerEventReceiver {
	public static void register() {
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.PAUSE_PIGLINS, PausePiglinsHandler::pause);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.UNPAUSE_PIGLINS, PausePiglinsHandler::unpause);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.CREATIVE_FOLLOW, CreativeFollowHandler::follow);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.CREATIVE_UNFOLLOW, CreativeFollowHandler::unfollow);
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.GROW_UP_PIGLINS, BabyPiglinGrowUpHandler::execute);
		ServerSidePacketRegistry.INSTANCE.register(ENABLE_DEATH_MESSAGES, PiglinDeathHandler::enable);
		ServerSidePacketRegistry.INSTANCE.register(DISABLE_DEATH_MESSAGES, PiglinDeathHandler::disable);
	}
}
