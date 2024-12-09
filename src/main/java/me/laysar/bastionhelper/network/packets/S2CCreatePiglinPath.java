package me.laysar.bastionhelper.network.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.packets.Helper.empty;

public record S2CCreatePiglinPath(int id, int currentNodeIndex, BlockPos[] positions, BlockPos target) {
	@Contract("_ -> new")
	public static @NotNull S2CCreatePiglinPath fromBuf(@NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		int currentNodeIndex = buf.readInt();
		int length = buf.readInt();

		BlockPos[] positions = new BlockPos[length];
		for (int i = 0; i < length; i++)
			positions[i] = buf.readBlockPos();

		BlockPos target = buf.readBlockPos();
		return new S2CCreatePiglinPath(id, currentNodeIndex, positions, target);
	}

	public @NotNull PacketByteBuf toBuf() {
		PacketByteBuf buf = empty();

		buf.writeInt(id);
		buf.writeInt(currentNodeIndex);
		buf.writeInt(positions.length);
		for (BlockPos pos : positions)
			buf.writeBlockPos(pos);
		buf.writeBlockPos(target);

		return buf;
	}
}
