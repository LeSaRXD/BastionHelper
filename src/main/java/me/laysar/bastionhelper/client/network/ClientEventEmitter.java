package me.laysar.bastionhelper.client.network;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ClientEventEmitter {
	private static @NotNull PacketByteBuf empty() {
		return new PacketByteBuf(Unpooled.buffer());
	}

	public static void highlightPiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(HIGHLIGHT_PIGLINS, empty());
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
}
