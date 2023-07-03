package me.nahkd.amethystenergy.modules.contexts;

import net.minecraft.item.ItemStack;

public class ModuleAttributeContext extends ModuleEventContext {
	public float attackDamage, attackSpeed;

	public ModuleAttributeContext(ItemStack stack, float attackDamage, float attackSpeed) {
		super(stack);
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
	}
}
