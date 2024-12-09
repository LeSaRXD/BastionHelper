package me.laysar.bastionhelper.network.packets;

import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.packets.Helper.empty;

public record S2CRemoveAggroLevel(int id) {
	@Contract("_ -> new")
	public static @NotNull S2CRemoveAggroLevel fromBuf(@NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		return new S2CRemoveAggroLevel(id);
	}

	public @NotNull PacketByteBuf toBuf() {
		PacketByteBuf buf = empty();

		buf.writeInt(id);

		return buf;
	}
}
