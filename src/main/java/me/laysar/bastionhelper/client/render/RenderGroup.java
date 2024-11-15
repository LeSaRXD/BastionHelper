package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.platform.GlStateManager;

import java.util.*;

public class RenderGroup<T extends Renderer> extends Renderer {
	private final int capacity;
	private final List<T> renderers = new ArrayList<>();
	private final Set<T> toAdd = new HashSet<>();
	private final Set<T> toRemove = new HashSet<>();

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
		if (renderers.size() >= capacity) return;
		toAdd.add(renderer);
	}

	public void remove(T renderer) {
		toRemove.add(renderer);
	}

	private void processQueues() {
		renderers.addAll(toAdd);
		toAdd.clear();
		renderers.removeAll(toRemove);
		toRemove.clear();
	}

	public void clear() {
		renderers.clear();
		toAdd.clear();
		toRemove.clear();
	}
}
