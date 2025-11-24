package me.laysar.bastionhelper.client.handler;

import me.laysar.bastionhelper.handler.AggroLevelsHandler.PiglinAggroLevel;
import me.laysar.bastionhelper.network.packets.S2CRemoveAggroLevel;
import me.laysar.bastionhelper.network.packets.S2CUpdateAggroLevel;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

	public static void update(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		S2CUpdateAggroLevel packet = S2CUpdateAggroLevel.fromBuf(buf);
		PiglinAggroLevel prevAggroLevel = aggroLevels.put(packet.id(), packet.aggroLevel());
		if (prevAggroLevel == packet.aggroLevel()) {
			return;
		}
		ShowPiglinPathsHandler.refreshPath(packet.id(), packet.aggroLevel());
	}

	public static void remove(@NotNull PacketContext _ctx, @NotNull PacketByteBuf buf) {
		S2CRemoveAggroLevel packet = S2CRemoveAggroLevel.fromBuf(buf);
		aggroLevels.remove(packet.id());
		ShowPiglinPathsHandler.removePath(packet.id());
	}

	public static PiglinAggroLevel getAggroLevel(int id) {
		return Objects.requireNonNullElse(aggroLevels.get(id), PiglinAggroLevel.NONE);
	}

	public static void clear() {
		aggroLevels.clear();
	}
}
