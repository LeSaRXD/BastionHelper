package me.laysar.bastionhelper.client.render;

import com.mojang.blaze3d.platform.GlStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RenderGroup<R extends Renderer> extends Renderer {
	private final int capacity;
	private final List<R> renderers = Collections.synchronizedList(new ArrayList<>());

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
		switch (option) {
			case FRONT:
				GlStateManager.disableDepthTest();
				break;
			case BACK:
				GlStateManager.enableDepthTest();
				break;
		}
		synchronized (renderers) {
			for (Renderer renderer : renderers)
				renderer.render();
		}
	}

	public void add(R renderer) {
		if (renderers.size() < capacity)
			renderers.add(renderer);
	}

	public void remove(R renderer) {
		renderers.removeIf(Predicate.isEqual(renderer));
	}

	public void clear() {
		renderers.clear();
	}
}
