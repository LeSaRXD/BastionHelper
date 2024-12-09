package me.laysar.bastionhelper.network.packets;

import me.laysar.bastionhelper.handler.AggroLevelsHandler.PiglinAggroLevel;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static me.laysar.bastionhelper.network.packets.Helper.empty;

public record S2CUpdateAggroLevel(int id, @NotNull PiglinAggroLevel aggroLevel) {
	@Contract("_ -> new")
	public static @NotNull S2CUpdateAggroLevel fromBuf(@NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		int aggroLevel = buf.readInt();
		return new S2CUpdateAggroLevel(id, PiglinAggroLevel.fromInt(aggroLevel));
	}

	public @NotNull PacketByteBuf toBuf() {
		PacketByteBuf buf = empty();

		buf.writeInt(id);
		buf.writeInt(aggroLevel.toInt());

		return buf;
	}
}
