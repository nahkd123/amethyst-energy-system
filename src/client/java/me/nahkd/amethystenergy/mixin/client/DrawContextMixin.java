package me.nahkd.amethystenergy.mixin.client;

import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.nahkd.amethystenergy.items.HasCustomBars;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
	@Inject(
			method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z")
			)
	private void aes$customBars(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
		var self = (DrawContext) (Object) this;

		if (stack.getItem() instanceof HasCustomBars hasCustomBars) {
			final var barX = x + 2;
			var barIndex = new AtomicInteger(0);

			hasCustomBars.emitCustomBars(stack, (color, progress) -> {
				var steps = Math.round(Math.max(Math.min(progress, 1f), 0f) * 13f);
				var barY = y + 13 - (barIndex.get() + (stack.isItemBarVisible()? 1 : 0)) * 2;

				self.fill(RenderLayer.getGuiOverlay(), barX, barY, barX + 13, barY + 2, -16777216);
				self.fill(RenderLayer.getGuiOverlay(), barX, barY, barX + steps, barY + 1, color | 0xFF000000);

				barIndex.addAndGet(1);
			});
		}
	}
}
