package me.laysar.bastionhelper.client.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import static me.laysar.bastionhelper.network.PacketIds.*;
import static me.laysar.bastionhelper.network.packets.Helper.empty;

public class ClientEventEmitter {
	public static void highlightPiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(HIGHLIGHT_PIGLINS, empty());
	}

	public static void lowlightPiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(LOWLIGHT_PIGLINS, empty());
	}

	public static void showPiglinPaths() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(SHOW_PIGLIN_PATHS, empty());
	}

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
}
