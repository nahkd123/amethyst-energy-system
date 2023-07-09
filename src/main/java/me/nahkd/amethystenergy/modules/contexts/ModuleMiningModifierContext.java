package me.nahkd.amethystenergy.modules.contexts;

import net.minecraft.item.ItemStack;

public class ModuleMiningModifierContext extends ModuleEventContext {
	public float miningSpeed;

	public ModuleMiningModifierContext(ItemStack stack, float miningSpeed) {
		super(stack);
		this.miningSpeed = miningSpeed;
	}
}
