package me.laysar.bastionhelper.mixin;

import me.laysar.bastionhelper.handler.PausePiglinsHandler;
import me.laysar.bastionhelper.handler.ShowPiglinPathsHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends LivingEntityMixin {
	@Override
	protected void onDeath(float health, CallbackInfo ci) {
		if (health <= 0.0f)
			ShowPiglinPathsHandler.remove(((LivingEntity) (Object) this).getEntityId());
	}

	@Override
	protected void onTick(CallbackInfo ci) {
		if (!PausePiglinsHandler.isPaused())
			return;

		ci.cancel();
		PiglinEntity piglin = ((PiglinEntity) (Object) this);
		MinecraftServer server = piglin.getServer();
		if (server == null) return;
		piglin.getBrain().tick(server.getWorld(piglin.world.getRegistryKey()), piglin);
	}
}
