package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.client.network.ClientEventEmitter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
	public static String KEY_CATEGORY = "key.category.bastionhelper";
	public static String KEY_HIGHLIGHT_PIGLINS = "key.bastionhelper.highlight_piglins";

	public static KeyBinding highlightPiglinsKey;

	public static void register() {
		highlightPiglinsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_HIGHLIGHT_PIGLINS,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_P,
				KEY_CATEGORY
		));

		registerKeyInputs();
	}

	private static void registerKeyInputs() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (highlightPiglinsKey.wasPressed()) {
				onHighlightPiglins(client);
				return;
			}
		});
	}

	private static void onHighlightPiglins(@NotNull MinecraftClient client) {
		if (!client.isInSingleplayer()) return;
		ClientPlayerEntity player = client.player;
		ClientWorld world = client.world;
		if (player == null || world == null) return;
		if (!world.isClient()) return;

		ClientEventEmitter.HighlightPiglins();
	}
}
