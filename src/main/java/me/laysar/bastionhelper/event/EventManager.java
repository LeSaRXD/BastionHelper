package me.laysar.bastionhelper.event;

import me.laysar.bastionhelper.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class EventManager {
	public static void register() {
		ServerLifecycleEvents.SERVER_STOPPING.register(EventManager::serverStopping);
	}

	private static void serverStopping(@NotNull MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(HighlightPiglinsHandler::lowlight);
		ShowPiglinPathsHandler.clear();
	}
}
