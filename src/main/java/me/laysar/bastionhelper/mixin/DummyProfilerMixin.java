package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.client.render.RenderQueue;
import net.minecraft.util.profiler.DummyProfiler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DummyProfiler.class)
public abstract class DummyProfilerMixin {
	@Unique
	private static final String SWAP_TYPE = "hand";

	@Inject(method = "swap(Ljava/lang/String;)V", at = @At(value = "HEAD"))
	void swap(String type, CallbackInfo ci) {
		if (type == null || !type.equals(SWAP_TYPE)) return;
		RenderQueue.get().render();
	}
}
