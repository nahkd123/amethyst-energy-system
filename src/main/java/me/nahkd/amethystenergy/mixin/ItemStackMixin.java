package me.nahkd.amethystenergy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import me.nahkd.amethystenergy.tools.AmethystTool;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Inject(
			method = "damage",
			at = @At("HEAD")
			)
	private void aes$damage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir, @Local LocalIntRef amountRef) {
		var self = (ItemStack) (Object) this;
		if (!(self.getItem() instanceof AmethystTool tool)) return;

		if (amountRef.get() <= 0) return;
		var ctx = new ModuleUseContext(self, amountRef.get(), player);
		ctx.getToolInstance().forEachModule(module -> module.getModuleType().onItemDamage(ctx, module));
		amountRef.set(ctx.durabilityUse);
	}
}
