package me.laysar.bastionhelper.client.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import static me.laysar.bastionhelper.network.PacketIds.*;
import static me.laysar.bastionhelper.network.packets.Helper.empty;

import me.laysar.bastionhelper.BastionHelper;

public class ClientEventEmitter {
	public static void pausePiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(PAUSE_PIGLINS, empty());
	}

	public static void unpausePiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(UNPAUSE_PIGLINS, empty());
	}

	public static void creativeFollow() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(CREATIVE_FOLLOW, empty());
	}

	public static void creativeUnfollow() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(CREATIVE_UNFOLLOW, empty());
	}

	public static void growUpPiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(GROW_UP_PIGLINS, empty());
	}

	public static void enableDeathMessages() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ENABLE_DEATH_MESSAGES, empty());
	}

	public static void disableDeathMessages() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(DISABLE_DEATH_MESSAGES, empty());
	}
}
