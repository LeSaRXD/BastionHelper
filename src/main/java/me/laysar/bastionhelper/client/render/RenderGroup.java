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
		BACK(0),
		FRONT(1),
		BACK_NO_CULL(2),
		FRONT_NO_CULL(3);
		final int value;
		RenderOption(int value) {
			this.value = value;
		}
		public boolean front() {
			return ((this.value >> 0) & 1) > 0;
		}
		public boolean cull() {
			return ((this.value >> 1) & 1) > 0;
		}
	}
	public RenderOption option;

	public RenderGroup(int capacity, RenderOption option) {
		assert capacity > 0;
		this.capacity = capacity;
		this.option = option;
	}

	@Override
	public void render() {
		if (option.front())
			GlStateManager.disableDepthTest();
		else
			GlStateManager.enableDepthTest();

		if (option.cull())
			GlStateManager.disableCull();
		else
			GlStateManager.enableCull();

		synchronized (renderers) {
			for (Renderer renderer : renderers)
				renderer.render();
		}

		GlStateManager.enableDepthTest();
		GlStateManager.enableCull();
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
