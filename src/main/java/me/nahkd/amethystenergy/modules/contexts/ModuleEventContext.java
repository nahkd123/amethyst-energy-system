package me.nahkd.amethystenergy.modules.contexts;

import me.nahkd.amethystenergy.tools.AmethystTool;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import net.minecraft.item.ItemStack;

public class ModuleEventContext {
	private ItemStack stack;
	private AmethystTool tool;

	public ModuleEventContext(ItemStack stack) {
		this.stack = stack;
		this.tool = (AmethystTool) stack.getItem();
	}

	public ItemStack getItemStack() {
		return stack;
	}

	public AmethystTool getToolType() {
		return tool;
	}

	public AmethystToolInstance getToolInstance() {
		return new AmethystToolInstance(stack, true);
	}
}
