package me.nahkd.amethystenergy.items;

import java.util.function.BiConsumer;

import net.minecraft.item.ItemStack;

public interface HasCustomBars {
	public void emitCustomBars(ItemStack stack, BiConsumer<Integer, Float> emitter);
}
