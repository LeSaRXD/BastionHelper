package me.laysar.bastionhelper.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public abstract class Helper {
	public static @NotNull PacketByteBuf empty() {
		return new PacketByteBuf(Unpooled.buffer());
	}
}
