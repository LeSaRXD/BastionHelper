package me.laysar.bastionhelper.client.keyboard;

import me.laysar.bastionhelper.client.handler.*;
import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class KeyInputManager {
	public static String KEY_CATEGORY = "key.category.bastionhelper";
	public static String HIGHLIGHT_PIGLINS = "key.bastionhelper.highlight_piglins";
	public static String SHOW_PIGLIN_PATHS = "key.bastionhelper.show_piglin_paths";
	public static String PAUSE_PIGLINS = "key.bastionhelper.pause_piglins";
	public static String CREATIVE_FOLLOW = "key.bastionhelper.creative_follow";
	public static String GROW_UP_PIGLINS = "key.bastionhelper.grow_up_piglins";

	public static void register() {
		KeyBind[] keyBinds = new KeyBind[] {
				new KeyBind(HIGHLIGHT_PIGLINS, GLFW.GLFW_KEY_O, HighlightPiglinsHandler::run),
				new KeyBind(SHOW_PIGLIN_PATHS, GLFW.GLFW_KEY_I, ShowPiglinPathsHandler::run),
				new KeyBind(PAUSE_PIGLINS, GLFW.GLFW_KEY_P, PausePiglinsHandler::run),
				new KeyBind(CREATIVE_FOLLOW, GLFW.GLFW_KEY_K, CreativeFollowHandler::run),
				new KeyBind(GROW_UP_PIGLINS, GLFW.GLFW_KEY_G, ClientEventEmitter::growUpPiglins),
		};

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// singleplayer-only keybinds
			if (!client.isInSingleplayer()) {
				return;
			}
			if (client.player == null || client.world == null) {
				return;
			}

			for (KeyBind keyBind : keyBinds) {
				keyBind.update();
			}
		});
	}
}
