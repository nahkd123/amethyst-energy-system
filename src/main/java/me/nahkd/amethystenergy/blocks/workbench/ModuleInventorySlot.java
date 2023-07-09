package me.nahkd.amethystenergy.blocks.workbench;

import me.nahkd.amethystenergy.modules.MatterCondenserVialModule;
import me.nahkd.amethystenergy.modules.Module;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
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
		if (!stack.hasNbt() || !stack.getNbt().contains(Module.TAG_MODULE, NbtElement.COMPOUND_TYPE)) return false;

		if (moduleType instanceof MatterCondenserVialModule && stack.hasNbt() && stack.getNbt().contains(Module.TAG_MODULE, NbtElement.COMPOUND_TYPE) && stack.getSubNbt(Module.TAG_MODULE).getFloat(MatterCondenserVialModule.TAG_CONDENSED) >= 1f) {
			var toolInstance = new AmethystToolInstance(workbench.tool.get(), false);
			var targetModules = toolInstance.getModules(workbench.moduleSlotTypes.get(getIndex() - 2));
			var targetModule = targetModules.get(workbench.moduleSlotIndexes.get(getIndex() - 2));

			if (!targetModule.isEmpty() && targetModule.getModuleType().canBeUpgraded() && !targetModule.getModuleType().isAlwaysPerfectModule()) {
				if (targetModule.getModuleQuality() >= 100) return false;
				return true;
			}
		}

		var slotType = workbench.moduleSlotTypes.get(getIndex() - 2);
		return moduleType.getModuleSlot().compatibleWithModuleSlot(slotType);
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return (getIndex() - 2) < workbench.moduleSlotIndexes.size() && !workbench.shards.isEmpty();
	}
}
