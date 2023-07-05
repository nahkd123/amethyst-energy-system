package me.nahkd.amethystenergy.inventory;

import net.minecraft.item.ItemStack;

public class StackHolder {
	private ItemStack stack = null;

	public ItemStack get() {
		return stack == null? ItemStack.EMPTY : stack;
	}

	public boolean set(ItemStack stack) {
		if (stack == ItemStack.EMPTY || stack.isEmpty()) {
			this.stack = null;
		} else {
			this.stack = stack.copy();
		}

		return true;
	}

	public void clear() {
		stack = null;
	}

	public boolean isEmpty() {
		return stack == null;
	}

	public ItemStack remove(int amount) {
		if (stack == null) return ItemStack.EMPTY;
		var removedStack = stack.copy();
		var toRemove = Math.min(stack.getCount(), amount);
		stack.setCount(stack.getCount() - toRemove);
		removedStack.setCount(toRemove);
		if (stack.getCount() <= 0) stack = null;
		return removedStack;
	}

	public boolean insertStack(ItemStack stack) {
		if (this.stack == null) {
			set(stack);
			return true;
		}

		if (ItemStack.canCombine(this.stack, stack)) {
			var newCount = Math.min(this.stack.getCount() + stack.getCount(), stack.getMaxCount());
			var leftover = this.stack.getCount() + stack.getCount() - newCount;
			this.stack.setCount(newCount);
			stack.setCount(leftover);
			if (stack.getCount() <= 0) return true;
		}

		return false;
	}
}
