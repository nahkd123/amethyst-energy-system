package me.nahkd.amethystenergy.modules.contexts;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ModuleUseContext extends ModuleEventContext {
	private LivingEntity user;
	public int durabilityUse;

	public ModuleUseContext(ItemStack stack, int durabilityUse, @Nullable LivingEntity user) {
		super(stack);
		this.durabilityUse = durabilityUse;
		this.user = user;
	}

	public @Nullable LivingEntity getUser() {
		return user;
	}
}
