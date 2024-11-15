package me.laysar.bastionhelper.client.network;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ClientEventEmitter {
	public static void highlightPiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(HIGHLIGHT_PIGLINS, new PacketByteBuf(Unpooled.buffer()));
	}
	public static void showPiglinPaths() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(SHOW_PIGLIN_PATHS, new PacketByteBuf(Unpooled.buffer()));
	}
}
