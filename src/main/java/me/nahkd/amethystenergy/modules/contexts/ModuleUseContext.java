package me.nahkd.amethystenergy.modules.contexts;

import me.nahkd.amethystenergy.modules.EnergyModule;
import me.nahkd.amethystenergy.modules.Module;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.tools.AmethystTool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class ModuleUseContext extends ModuleEventContext {
	private NbtCompound energyModule;
	private float energyCost = 0f;
	private float energyLeft = 0f;
	public int durabilityUse;

	public ModuleUseContext(ItemStack stack, int durabilityUse) {
		super(stack);
		this.durabilityUse = durabilityUse;

		if (stack.hasNbt() && stack.getNbt().contains(AmethystTool.TAG_MODULES, NbtElement.LIST_TYPE)) {
			var modules = stack.getNbt().getList(AmethystTool.TAG_MODULES, NbtElement.COMPOUND_TYPE);
			var binding = getToolType().getFirstModule(modules, ModuleSlot.BINDING);
			var bindingType = Module.getModuleType(binding);

			if (bindingType != null && bindingType instanceof EnergyModule) {
				energyModule = binding;
				energyLeft = binding.getFloat(EnergyModule.TAG_ENERGY);
			}
		}
	}

	public float getEnergyCost() {
		return energyCost;
	}

	public void setEnergyCost(float amount) {
		energyCost = amount;
	}

	public boolean addEnergyCost(float amount) {
		if ((energyCost + amount) > energyLeft) return false;
		energyCost += amount;
		return true;
	}

	public void applyUpdates() {
		if (energyModule != null) energyModule.putFloat(EnergyModule.TAG_ENERGY, energyLeft - energyCost);
	}
}
