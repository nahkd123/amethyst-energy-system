package me.nahkd.amethystenergy.items;

import net.minecraft.item.ItemStack;

public interface HasCustomBars {
	public int getBarsCount(ItemStack stack);
	public int getBarColor(ItemStack stack, int index);
	public float getBarProgress(ItemStack stack, int index);
}
