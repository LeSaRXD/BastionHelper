package me.laysar.bastionhelper.network.packets;

import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.packets.Helper.empty;

public record S2CRemovePiglinPath(int id) {
	@Contract("_ -> new")
	public static @NotNull S2CRemovePiglinPath fromBuf(@NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		return new S2CRemovePiglinPath(id);
	}


	public @NotNull PacketByteBuf toBuf() {
		PacketByteBuf buf = empty();

		buf.writeInt(id);

		return buf;
	}
}
