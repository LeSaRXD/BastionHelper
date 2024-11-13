package me.laysar.bastionhelper.client.network;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;

import static me.laysar.bastionhelper.network.PacketIds.HIGHLIGHT_PIGLINS;

public class ClientEventEmitter {
	public static void HighlightPiglins() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(HIGHLIGHT_PIGLINS, new PacketByteBuf(Unpooled.buffer()));
	}
}
