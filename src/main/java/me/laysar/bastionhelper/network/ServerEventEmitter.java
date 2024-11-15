package me.laysar.bastionhelper.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static me.laysar.bastionhelper.network.PacketIds.*;

public class ServerEventEmitter {
	public static void updatePiglinPath(@NotNull PlayerEntity player, int id, @NotNull Path newPath) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeInt(id);

		int nextNode = newPath.getCurrentNodeIndex() + 1;
		int remainingLength = newPath.getLength() - nextNode;
		buf.writeInt(remainingLength);

		List<PathNode> remainingNodes = newPath.getNodes().stream().skip(nextNode).toList();
		for (PathNode node : remainingNodes) {
			buf.writeBlockPos(node.getPos());
		}
		buf.writeBlockPos(newPath.getTarget());

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, buf);
	}

	public static void removePiglinPath(@NotNull PlayerEntity player, int id) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(id);

		buf.writeInt(0);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, UPDATE_PIGLIN_PATH, buf);
	}
}
