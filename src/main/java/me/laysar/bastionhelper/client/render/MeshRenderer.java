package me.laysar.bastionhelper.client.render;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

public class MeshRenderer extends Renderer {

	protected static final int DRAW_MODE = 7;
	private static final Vec3d ONE = new Vec3d(1.0, 1.0, 1.0),
			OFFSET = new Vec3d(0.5, 0.5, 0.5);

	private static class PositionInfo {

		public final MutableInt count = new MutableInt(1);
		public boolean up, down, north, south, east, west;

		public PositionInfo(boolean up, boolean down, boolean north, boolean south, boolean east, boolean west) {
			this.up = up;
			this.down = down;
			this.north = north;
			this.south = south;
			this.east = east;
			this.west = west;
		}
	}

	private final Map<Vec3i, PositionInfo> positionCounts = Collections.synchronizedMap(new HashMap<>());

	private record Face(@NotNull Vec3i pos, @NotNull Direction dir) {

	}

	private final Set<Face> meshFaces = Collections.synchronizedSet(new HashSet<>());

	private final Color color;

	public MeshRenderer(@NotNull Color color) {
		this.color = color;
	}

	private void updateMesh() {
		synchronized (meshFaces) {
			meshFaces.clear();
			if (positionCounts.isEmpty()) {
				return;
			}
			positionCounts.forEach((pos, info) -> {
				if (!info.up) {
					meshFaces.add(new Face(pos, Direction.UP));
				}
				if (!info.down) {
					meshFaces.add(new Face(pos, Direction.DOWN));
				}
				if (!info.north) {
					meshFaces.add(new Face(pos, Direction.NORTH));
				}
				if (!info.south) {
					meshFaces.add(new Face(pos, Direction.SOUTH));
				}
				if (!info.east) {
					meshFaces.add(new Face(pos, Direction.EAST));
				}
				if (!info.west) {
					meshFaces.add(new Face(pos, Direction.WEST));
				}
			});
		}
	}

	public void addPositions(@NotNull Vec3i bottomLeft, @NotNull Vec3i topRight) {
		synchronized (positionCounts) {
			boolean shouldUpdateMesh = false;
			for (int i = bottomLeft.getX(); i <= topRight.getX(); i++) {
				for (int j = bottomLeft.getY(); j <= topRight.getY(); j++) {
					for (int k = bottomLeft.getZ(); k <= topRight.getZ(); k++) {
						shouldUpdateMesh |= addPosition(new Vec3i(i, j, k));
					}
				}
			}
			if (shouldUpdateMesh) {
				updateMesh();
			}
		}
	}

	private boolean addPosition(@NotNull Vec3i pos) {
		if (positionCounts.containsKey(pos)) {
			positionCounts.get(pos).count.increment();
			return false;
		}

		PositionInfo up = positionCounts.get(pos.offset(Direction.UP, 1)),
				down = positionCounts.get(pos.offset(Direction.DOWN, 1)),
				north = positionCounts.get(pos.offset(Direction.NORTH, 1)),
				south = positionCounts.get(pos.offset(Direction.SOUTH, 1)),
				east = positionCounts.get(pos.offset(Direction.EAST, 1)),
				west = positionCounts.get(pos.offset(Direction.WEST, 1));

		PositionInfo newInfo = new PositionInfo(
				up != null,
				down != null,
				north != null,
				south != null,
				east != null,
				west != null
		);
		positionCounts.put(pos, newInfo);

		if (up != null) {
			up.down = true;
		}
		if (down != null) {
			down.up = true;
		}
		if (north != null) {
			north.south = true;
		}
		if (south != null) {
			south.north = true;
		}
		if (east != null) {
			east.west = true;
		}
		if (west != null) {
			west.east = true;
		}

		return true;
	}

	public void removePositions(@NotNull Vec3i bottomLeft, @NotNull Vec3i topRight) {
		synchronized (positionCounts) {
			boolean shouldUpdateMesh = false;
			for (int i = bottomLeft.getX(); i <= topRight.getX(); i++) {
				for (int j = bottomLeft.getY(); j <= topRight.getY(); j++) {
					for (int k = bottomLeft.getZ(); k <= topRight.getZ(); k++) {
						shouldUpdateMesh |= removePosition(new Vec3i(i, j, k));
					}
				}
			}
			if (shouldUpdateMesh) {
				updateMesh();
			}
		}
	}

	private boolean removePosition(@NotNull Vec3i pos) {
		if (!positionCounts.containsKey(pos)) {
			return false;
		}

		PositionInfo info = positionCounts.get(pos);
		info.count.decrement();
		if (info.count.intValue() > 0) {
			return false;
		}

		positionCounts.remove(pos);

		PositionInfo up = positionCounts.get(pos.offset(Direction.UP, 1)),
				down = positionCounts.get(pos.offset(Direction.DOWN, 1)),
				north = positionCounts.get(pos.offset(Direction.NORTH, 1)),
				south = positionCounts.get(pos.offset(Direction.SOUTH, 1)),
				east = positionCounts.get(pos.offset(Direction.EAST, 1)),
				west = positionCounts.get(pos.offset(Direction.WEST, 1));

		if (up != null) {
			up.down = false;
		}
		if (down != null) {
			down.up = false;
		}
		if (north != null) {
			north.south = false;
		}
		if (south != null) {
			south.north = false;
		}
		if (east != null) {
			east.west = false;
		}
		if (west != null) {
			west.east = false;
		}

		return true;
	}

	@Override
	public void render() {
		enableTransparency();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		buffer.begin(DRAW_MODE, VertexFormats.POSITION_COLOR);

		for (Face face : meshFaces) {
			addFace(buffer, Vec3d.of(face.pos).add(OFFSET), ONE, face.dir, color);
		}

		tessellator.draw();

		disableTransparency();
	}

	public void clear() {
		positionCounts.clear();
		meshFaces.clear();
	}
}
