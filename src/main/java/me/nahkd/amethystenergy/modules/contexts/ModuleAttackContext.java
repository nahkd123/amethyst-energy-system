package me.nahkd.amethystenergy.modules.contexts;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ModuleAttackContext extends ModuleUseContext {
	public LivingEntity target, attacker;

	public ModuleAttackContext(ItemStack stack, int durabilityUse, LivingEntity target, LivingEntity attacker) {
		super(stack, durabilityUse, attacker);
		this.target = target;
		this.attacker = attacker;
	}
}
