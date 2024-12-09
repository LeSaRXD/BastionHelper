package me.laysar.bastionhelper.network.packets;

import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.packets.Helper.empty;

public record S2CUpdatePiglinPath(int id, int currentNodeIndex) {
	@Contract("_ -> new")
	public static @NotNull S2CUpdatePiglinPath fromBuf(@NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		int currentNodeIndex = buf.readInt();

		return new S2CUpdatePiglinPath(id, currentNodeIndex);
	}

	public @NotNull PacketByteBuf toBuf() {
		PacketByteBuf buf = empty();

		buf.writeInt(id);
		buf.writeInt(currentNodeIndex);
		return buf;
	}
}
