package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ServerEventReceiver {
	public static void register() {
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.HIGHLIGHT_PIGLINS,
				(ctx, _buf) -> HighlightPiglinsHandler.run(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.SHOW_PIGLIN_PATHS,
				(ctx, _buf) -> ShowPiglinPathsHandler.run(ctx.getPlayer()));
	}
}
