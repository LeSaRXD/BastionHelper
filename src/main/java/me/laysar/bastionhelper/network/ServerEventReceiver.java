package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.handler.CreativeFollowHandler;
import me.laysar.bastionhelper.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.handler.PausePiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ServerEventReceiver {
	public static void register() {
		ServerSidePacketRegistry.INSTANCE.register(PacketIds.HIGHLIGHT_PIGLINS,
				(ctx, _buf) -> HighlightPiglinsHandler.highlight(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.LOWLIGHT_PIGLINS,
				(ctx, _buf) -> HighlightPiglinsHandler.lowlight(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.SHOW_PIGLIN_PATHS,
				(ctx, _buf) -> ShowPiglinPathsHandler.run(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.PAUSE_PIGLINS,
				(ctx, _buf) -> PausePiglinsHandler.pause(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.UNPAUSE_PIGLINS,
				(ctx, _buf) -> PausePiglinsHandler.unpause(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.CREATIVE_FOLLOW,
				(ctx, _buf) -> CreativeFollowHandler.follow(ctx.getPlayer()));

		ServerSidePacketRegistry.INSTANCE.register(PacketIds.CREATIVE_UNFOLLOW,
				(ctx, _buf) -> CreativeFollowHandler.unfollow(ctx.getPlayer()));
	}
}
