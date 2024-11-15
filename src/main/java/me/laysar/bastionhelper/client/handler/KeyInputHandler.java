package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
	public static String KEY_CATEGORY = "key.category.bastionhelper";
	public static String KEY_HIGHLIGHT_PIGLINS = "key.bastionhelper.highlight_piglins";
	public static String KEY_SHOW_PIGLIN_PATHS = "key.bastionhelper.show_piglin_paths";

	public static KeyBinding highlightPiglinsKey;
	public static KeyBinding showPiglinPathfindingKey;

	public static void register() {
		highlightPiglinsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_HIGHLIGHT_PIGLINS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_P,
				KEY_CATEGORY
		));
		showPiglinPathfindingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_SHOW_PIGLIN_PATHS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_L,
				KEY_CATEGORY
		));

		registerKeyInputs();
	}

	private static void registerKeyInputs() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (highlightPiglinsKey.wasPressed()) {
				if (!client.isInSingleplayer()) return;
				if (client.player == null || client.world == null) return;

				ClientEventEmitter.highlightPiglins();
			} else if (showPiglinPathfindingKey.wasPressed()) {
				if (!client.isInSingleplayer()) return;
				if (client.player == null || client.world == null) return;

				ShowPiglinPathsHandler.run();
			}
		});
	}
}
