package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.BastionHelper;
import net.minecraft.util.Identifier;

public class PacketIds {
	public static final Identifier HIGHLIGHT_PIGLINS = new Identifier(BastionHelper.MOD_ID, "highlight_piglins");
	public static final Identifier SHOW_PIGLIN_PATHS = new Identifier(BastionHelper.MOD_ID, "show_piglin_paths");
	public static final Identifier CREATE_PIGLIN_PATH = new Identifier(BastionHelper.MOD_ID, "create_piglin_path");
	public static final Identifier UPDATE_PIGLIN_PATH = new Identifier(BastionHelper.MOD_ID, "update_piglin_path");
	public static final Identifier REMOVE_PIGLIN_PATH = new Identifier(BastionHelper.MOD_ID, "remove_piglin_path");
	public static final Identifier PAUSE_PIGLINS = new Identifier(BastionHelper.MOD_ID, "pause_piglins");
	public static final Identifier UNPAUSE_PIGLINS = new Identifier(BastionHelper.MOD_ID, "unpause_piglins");
	public static final Identifier UPDATE_AGGRO_LEVEL = new Identifier(BastionHelper.MOD_ID, "update_aggro_level");
	public static final Identifier REMOVE_AGGRO_LEVEL = new Identifier(BastionHelper.MOD_ID, "remove_aggro_level");
	public static final Identifier CREATIVE_FOLLOW = new Identifier(BastionHelper.MOD_ID, "creative_follow");
	public static final Identifier CREATIVE_UNFOLLOW = new Identifier(BastionHelper.MOD_ID, "creative_unfollow");
	public static final Identifier GROW_UP_PIGLINS = new Identifier(BastionHelper.MOD_ID, "grow_up");
}
