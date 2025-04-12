package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

	public static String KEY_CATEGORY = "key.category.bastionhelper";
	public static String KEY_HIGHLIGHT_PIGLINS = "key.bastionhelper.highlight_piglins";
	public static String KEY_SHOW_PIGLIN_PATHS = "key.bastionhelper.show_piglin_paths";
	public static String KEY_PAUSE_PIGLINS = "key.bastionhelper.pause_piglins";
	public static String KEY_SHOW_LAVA_DEADZONES = "key.bastionhelper.show_lava_deadzones";
	public static String KEY_CREATIVE_FOLLOW = "key.bastionhelper.creative_follow";
	public static String KEY_GROW_UP_PIGLINS = "key.bastionhelper.grow_up_piglins";

	public static KeyBinding highlightPiglinsKey;
	public static KeyBinding showPiglinPathfindingKey;
	public static KeyBinding pausePiglinsKey;
	public static KeyBinding showLavaDeadzonesKey;
	public static KeyBinding creativeFollowKey;
	public static KeyBinding growUpPiglinsKey;

	public static void register() {
		highlightPiglinsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_HIGHLIGHT_PIGLINS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_O,
				KEY_CATEGORY
		));
		showPiglinPathfindingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_SHOW_PIGLIN_PATHS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_I,
				KEY_CATEGORY
		));
		pausePiglinsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_PAUSE_PIGLINS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_P,
				KEY_CATEGORY
		));
		showLavaDeadzonesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_SHOW_LAVA_DEADZONES,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_L,
				KEY_CATEGORY
		));
		creativeFollowKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_CREATIVE_FOLLOW,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				KEY_CATEGORY
		));
		growUpPiglinsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_GROW_UP_PIGLINS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_G,
				KEY_CATEGORY
		));

		registerKeyInputs();
	}

	private static void registerKeyInputs() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// singleplayer-only keybinds
			if (!client.isInSingleplayer()) {
				return;
			}
			if (client.player == null || client.world == null) {
				return;
			}

			if (highlightPiglinsKey.wasPressed()) {
				HighlightPiglinsHandler.run();
			}
			if (showPiglinPathfindingKey.wasPressed()) {
				ShowPiglinPathsHandler.run();
			}
			if (pausePiglinsKey.wasPressed()) {
				PausePiglinsHandler.run();
			}
			if (showLavaDeadzonesKey.wasPressed()) {
				ShowLavaDeadzonesHandler.run();
			}
			if (creativeFollowKey.wasPressed()) {
				CreativeFollowHandler.run();
			}
			if (growUpPiglinsKey.wasPressed()) {
				ClientEventEmitter.growUpPiglins();
			}
		});
	}
}
