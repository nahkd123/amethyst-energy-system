package me.nahkd.amethystenergy.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface SimpleInventory extends Inventory {
	DefaultedList<ItemStack> getItems();

	@Override
	default int size() {
		return getItems().size();
	}

	@Override
	default boolean isEmpty() {
		return getItems().stream().allMatch(stack -> isEmpty());
	}

	@Override
	default ItemStack getStack(int i) {
		return getItems().get(i);
	}

	@Override
	default ItemStack removeStack(int slot) {
		return Inventories.removeStack(getItems(), slot);
	}

	@Override
	default ItemStack removeStack(int slot, int count) {
		var result = Inventories.splitStack(getItems(), slot, count);
		if (!result.isEmpty()) markDirty();
		return result;
	}

	@Override
	default void setStack(int slot, ItemStack stack) {
		getItems().set(slot, stack);
		if (stack.getCount() > stack.getMaxCount()) stack.setCount(stack.getMaxCount());
	}

	@Override
	default void clear() {
		getItems().clear();
	}

	@Override
	default void markDirty() {
		// NO-OP
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}
