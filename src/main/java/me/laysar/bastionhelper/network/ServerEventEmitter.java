package me.laysar.bastionhelper.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ServerEventEmitter {
	private static @NotNull PacketByteBuf empty() {
		return new PacketByteBuf(Unpooled.buffer());
	}

	public static void confirmHighlight(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, HIGHLIGHT_PIGLINS, empty());
	}

	public static void confirmLowlight(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, LOWLIGHT_PIGLINS, empty());
	}

	public static void createPiglinPath(@NotNull PlayerEntity player, int id, @NotNull Path path, int aggroLevel) {
		PacketByteBuf buf = empty();

		buf.writeInt(id);
		buf.writeInt(path.getCurrentNodeIndex());
		buf.writeInt(path.getLength());

		for (PathNode node : path.getNodes())
			buf.writeBlockPos(node.getPos());
		buf.writeBlockPos(path.getTarget());

		buf.writeInt(aggroLevel);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CREATE_PIGLIN_PATH, buf);
	}

	public static void updatePiglinPath(@NotNull PlayerEntity player, int id, int currentNodeIndex, int aggroLevel) {
		PacketByteBuf buf = empty();

		buf.writeInt(id);
		buf.writeInt(currentNodeIndex);
		buf.writeInt(aggroLevel);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, buf);
	}

	public static void removePiglinPath(@NotNull PlayerEntity player, int id) {
		PacketByteBuf buf = empty();

		buf.writeInt(id);
		buf.writeInt(-1);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, buf);
	}

	public static void confirmPause(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PAUSE_PIGLINS, empty());
	}

	public static void confirmUnpause(@NotNull PlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UNPAUSE_PIGLINS, empty());
	}
}
