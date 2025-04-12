package me.laysar.bastionhelper.network;

import me.laysar.bastionhelper.handler.AggroLevelsHandler.PiglinAggroLevel;
import me.laysar.bastionhelper.network.packets.*;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.PacketIds.*;
import static me.laysar.bastionhelper.network.packets.Helper.empty;

public class ServerEventEmitter {
	public static void toggleHighlights(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, HIGHLIGHT_PIGLINS, empty());
	}

	public static void createPiglinPath(@NotNull PlayerEntity player, int id, @NotNull Path path) {
		S2CCreatePiglinPath packet = new S2CCreatePiglinPath(
				id,
				path.getCurrentNodeIndex(),
				path.getNodes().stream().map(PathNode::getPos).toList().toArray(new BlockPos[path.getLength()]),
				path.getTarget()
		);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CREATE_PIGLIN_PATH, packet.toBuf());
	}

	public static void updatePiglinPath(@NotNull PlayerEntity player, int id, int currentNodeIndex) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, new S2CUpdatePiglinPath(id, currentNodeIndex).toBuf());
	}

	public static void removePiglinPath(@NotNull PlayerEntity player, int id) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, REMOVE_PIGLIN_PATH, new S2CRemovePiglinPath(id).toBuf());
	}

	public static void confirmPause(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PAUSE_PIGLINS, empty());
	}

	public static void confirmUnpause(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UNPAUSE_PIGLINS, empty());
	}

	public static void updateAggroLevel(@NotNull PlayerEntity player, int id, @NotNull PiglinAggroLevel aggroLevel) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_AGGRO_LEVEL, new S2CUpdateAggroLevel(id, aggroLevel).toBuf());
	}

	public static void removeAggroLevel(@NotNull PlayerEntity player, int id) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, REMOVE_AGGRO_LEVEL, new S2CRemoveAggroLevel(id).toBuf());
	}

	public static void confirmFollow(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CREATIVE_FOLLOW, empty());
	}

	public static void confirmUnfollow(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CREATIVE_UNFOLLOW, empty());
	}
}
