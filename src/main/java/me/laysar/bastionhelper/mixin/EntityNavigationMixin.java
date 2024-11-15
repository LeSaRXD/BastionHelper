package me.laysar.bastionhelper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.laysar.bastionhelper.BastionHelper;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityNavigation.class)
public abstract class EntityNavigationMixin {
	@Shadow @Final protected MobEntity entity;

	@Shadow @Nullable public abstract Path getCurrentPath();

	@ModifyReturnValue(method = "findPathToAny(Ljava/util/Set;IZI)Lnet/minecraft/entity/ai/pathing/Path;", at = @At("RETURN"))
	Path findPathToAny(Path original) {
		if (!(this.entity instanceof PiglinEntity piglin)) return original;

		ShowPiglinPathsHandler.newPath(piglin.getEntityId(), original);
		return original;
	}

	@Inject(method = "*",
	at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/Path;setCurrentNodeIndex(I)V"))
	void setCurrentNodeIndex(CallbackInfo ci) {
		if (!(this.entity instanceof PiglinEntity piglin)) return;

		ShowPiglinPathsHandler.updatePath(piglin.getEntityId(), this.getCurrentPath());
	}

	@Inject(method = "stop()V", at = @At("HEAD"))
	void stop(CallbackInfo ci) {
		if (!(this.entity instanceof PiglinEntity piglin)) return;

		ShowPiglinPathsHandler.removePath(piglin.getEntityId());
	}
}
