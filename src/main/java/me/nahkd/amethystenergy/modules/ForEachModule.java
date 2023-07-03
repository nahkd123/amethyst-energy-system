package me.nahkd.amethystenergy.modules;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface ForEachModule {
	void accept(Module type, int quality, NbtCompound moduleData);
}
