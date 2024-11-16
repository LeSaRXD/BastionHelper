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
	public static void createPiglinPath(@NotNull PlayerEntity player, int id, @NotNull Path path) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeInt(id);

		buf.writeInt(path.getLength());

		for (PathNode node : path.getNodes())
			buf.writeBlockPos(node.getPos());
		buf.writeBlockPos(path.getTarget());

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CREATE_PIGLIN_PATH, buf);

		if (path.getCurrentNodeIndex() > 0) updatePiglinPath(player, id, path.getCurrentNodeIndex());
	}

	public static void updatePiglinPath(@NotNull PlayerEntity player, int id, int currentNodeIndex) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeInt(id);
		buf.writeInt(currentNodeIndex);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, buf);
	}

	public static void removePiglinPath(@NotNull PlayerEntity player, int id) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(id);

		buf.writeInt(-1);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, buf);
	}
}
