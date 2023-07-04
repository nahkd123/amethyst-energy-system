package me.nahkd.amethystenergy.modules.contexts;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ModuleUseContext extends ModuleEventContext {
	private LivingEntity user;
	public int durabilityUse;

	public ModuleUseContext(ItemStack stack, int durabilityUse, LivingEntity user) {
		super(stack);
		this.durabilityUse = durabilityUse;
		this.user = user;
	}

	public LivingEntity getUser() {
		return user;
	}
}
