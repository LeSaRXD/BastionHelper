package me.laysar.bastionhelper.client.render;

import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RenderQueue {
	private static RenderQueue INSTANCE;

	public static RenderQueue get() {
		if (INSTANCE == null)
			INSTANCE = new RenderQueue();
		return INSTANCE;
	}

	public MatrixStack matrixStack = null;
	private final List<Consumer<MatrixStack>> consumerList = new ArrayList<>();

	public void add(@NotNull Consumer<MatrixStack> consumer) {
		consumerList.add(consumer);
	}

	public void remove(@NotNull Consumer<MatrixStack> consumer) {
		consumerList.remove(consumer);
	}

	public void render() {
		if (this.matrixStack == null) return;

		for (Consumer<MatrixStack> consumer : this.consumerList) {
			consumer.accept(this.matrixStack);
		}
	}
}
