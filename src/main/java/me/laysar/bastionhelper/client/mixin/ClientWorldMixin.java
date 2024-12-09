package me.laysar.bastionhelper.client.mixin;

import me.laysar.bastionhelper.client.handler.ShowLavaDeadzonesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends WorldMixin {
	@Override
	protected void onBlockChanged(@NotNull BlockPos pos, @NotNull BlockState oldBlock, @NotNull BlockState newBlock, CallbackInfo ci) {
		if (newBlock.isOf(Blocks.LAVA)) {
			ShowLavaDeadzonesHandler.addLava(pos);
		} else if (oldBlock.isOf(Blocks.LAVA)) {
			ShowLavaDeadzonesHandler.removeLava(pos);
		}
	}
}
