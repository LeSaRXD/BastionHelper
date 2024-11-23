package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler.PiglinAggroLevel;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AggroLevelsHandler {
	private static final Map<Integer, PiglinAggroLevel> aggroLevels = Collections.synchronizedMap(new HashMap<>());
	public static int[] aggroLevelCounts() {
		int[] counts = {0, 0, 0, 0};
		synchronized (aggroLevels) {
			for (PiglinAggroLevel aggroLevel : aggroLevels.values()) {
				if (aggroLevel == PiglinAggroLevel.NONE) continue;
				counts[aggroLevel.toInt() - 1]++;
			}
		}
		return counts;
	}

	public static void update(@NotNull PacketContext ctx, @NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		PiglinAggroLevel aggroLevel = PiglinAggroLevel.fromInt(buf.readInt());
		aggroLevels.put(id, aggroLevel);
	}
	public static void remove(@NotNull PacketContext ctx, @NotNull PacketByteBuf buf) {
		int id = buf.readInt();
		aggroLevels.remove(id);
	}
}
