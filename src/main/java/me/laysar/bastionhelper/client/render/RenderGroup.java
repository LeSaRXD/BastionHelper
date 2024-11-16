package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.Pair;

import java.util.*;

public class RenderGroup<T extends Renderer> extends Renderer {
	private final int capacity;
	private final Set<T> renderers = new HashSet<>();
	private final List<Pair<Boolean, T>> queue = new ArrayList<>();

	public enum RenderOption {
		FRONT,
		BACK
	}
	public RenderOption option;

	public RenderGroup(int capacity, RenderOption option) {
		assert capacity > 0;
		this.capacity = capacity;
		this.option = option;
	}

	@Override
	public void render() {
		processQueues();

		switch (option) {
			case FRONT:
				GlStateManager.disableDepthTest();
				break;
			case BACK:
				GlStateManager.enableDepthTest();
				break;
		}
		for (Renderer renderer : renderers)
			renderer.render();
	}

	public void add(T renderer) {
		queue.add(new Pair<>(true, renderer));
	}

	public void remove(T renderer) {
		if (renderer == null) return;
		queue.add(new Pair<>(false, renderer));
	}

	private void processQueues() {
		for (Pair<Boolean, T> pair : queue) {
			if (pair.getLeft()) {
				if (renderers.size() < capacity)
					renderers.add(pair.getRight());
			}
			else
				renderers.remove(pair.getRight());
		}
		queue.clear();
	}

	public void clear() {
		renderers.clear();
		queue.clear();
	}
}
