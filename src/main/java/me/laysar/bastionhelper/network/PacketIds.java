package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.BastionHelper;
import net.minecraft.util.Identifier;

public class PacketIds {
	public static final Identifier HIGHLIGHT_PIGLINS = new Identifier(BastionHelper.MOD_ID, "highlight_piglins");
	public static final Identifier SHOW_PIGLIN_PATHS = new Identifier(BastionHelper.MOD_ID, "show_piglin_paths");
	public static final Identifier UPDATE_PIGLIN_PATH = new Identifier(BastionHelper.MOD_ID, "update_piglin_paths");
}
