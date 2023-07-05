package me.nahkd.amethystenergy.blocks.workbench;

import me.nahkd.amethystenergy.modules.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ModuleInventorySlot extends Slot {
	private AmethystWorkbenchScreenHandler workbench;

	public ModuleInventorySlot(AmethystWorkbenchScreenHandler workbench, int index, int x, int y) {
		super(workbench, index, x, y);
		this.workbench = workbench;
	}

	@Override
	public boolean isEnabled() {
		return (getIndex() - 2) < workbench.moduleSlotIndexes.size();
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		if (stack == null || stack.isEmpty()) return true;

		if ((getIndex() - 2) >= workbench.moduleSlotIndexes.size()) return false;
		if (!(stack.getItem() instanceof Module moduleType)) return false;
		if (workbench.shards.isEmpty() || workbench.shards.get().getCount() < moduleType.shardsApplyCost()) return false;

		var slotType = workbench.moduleSlotTypes.get(getIndex() - 2);
		return slotType == moduleType.getModuleSlot();
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return (getIndex() - 2) < workbench.moduleSlotIndexes.size() && !workbench.shards.isEmpty();
	}
}
