package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.handler.HighlightPiglinsHandler;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ServerEventReceiver {
	public static void register() {
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.HIGHLIGHT_PIGLINS,
				(ctx, _buf) -> HighlightPiglinsHandler.run(ctx.getPlayer()));
	}
}
