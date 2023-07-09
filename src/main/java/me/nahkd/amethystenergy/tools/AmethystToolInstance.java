package me.nahkd.amethystenergy.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import me.nahkd.amethystenergy.aes.AmethystEnergyInterface;
import me.nahkd.amethystenergy.modules.EnergyModule;
import me.nahkd.amethystenergy.modules.ModuleInstance;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class AmethystToolInstance implements AmethystEnergyInterface {
	private ItemStack stack;
	private AmethystTool tool;
	private NbtCompound moduleSlots;
	private boolean mutateNbt;

	public AmethystToolInstance(ItemStack stack, boolean mutateNbt) {
		this.stack = stack;
		this.tool = (AmethystTool) stack.getItem();
		this.mutateNbt = mutateNbt;

		var nbt = mutateNbt? stack.getOrCreateNbt() : stack.hasNbt()? stack.getNbt() : new NbtCompound();
		moduleSlots = nbt.contains(AmethystTool.TAG_MODULES, NbtElement.COMPOUND_TYPE)? nbt.getCompound(AmethystTool.TAG_MODULES) : tool.createEmptySlots();
		if (mutateNbt && !nbt.contains(AmethystTool.TAG_MODULES, NbtElement.COMPOUND_TYPE)) nbt.put(AmethystTool.TAG_MODULES, moduleSlots);
	}

	public ItemStack getItemStack() {
		return stack;
	}

	public AmethystTool getToolType() {
		return tool;
	}

	public List<ModuleInstance> getModules(ModuleSlot slot) {
		NbtList modulesData;
		List<ModuleInstance> modules = new ArrayList<>();

		if (!moduleSlots.contains(slot.slotName, NbtElement.LIST_TYPE)) {
			var count = tool.getSlots().getOrDefault(slot, 0);
			if (tool.getSlots().getOrDefault(slot, 0) == 0) return Arrays.asList();

			modulesData = new NbtList();
			for (int i = 0; i < count; i++) modulesData.add(new NbtCompound());
			if (mutateNbt) moduleSlots.put(slot.slotName, modulesData);
		} else {
			modulesData = moduleSlots.getList(slot.slotName, NbtElement.COMPOUND_TYPE);
		}

		for (int i = 0; i < tool.getSlots().getOrDefault(slot, 0); i++) {
			var moduleData = modulesData.getCompound(i);
			modules.add(new ModuleInstance(moduleData));
			if (i >= modulesData.size() && mutateNbt) modulesData.add(moduleData);
		}

		return Collections.unmodifiableList(modules);
	}

	public List<ModuleInstance> getAllModules() {
		List<ModuleInstance> modules = new ArrayList<>();

		for (var slot : tool.getSlots().keySet()) {
			var modulesData = moduleSlots.getList(slot.slotName, NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < modulesData.size(); i++) modules.add(new ModuleInstance(modulesData.getCompound(i))); 
		}

		return Collections.unmodifiableList(modules);
	}

	public void forEachModule(Consumer<ModuleInstance> consumer) {
		List<List<ModuleInstance>> stages = new ArrayList<>();
		for (int i = 0; i < 3; i++) stages.add(new ArrayList<>());

		if (stack.hasNbt() && stack.getNbt().contains(AmethystTool.TAG_MODULES, NbtElement.COMPOUND_TYPE)) {
        	var modules = getAllModules();

        	for (int i = 0; i < modules.size(); i++) {
        		var module = modules.get(i);
        		if (module == null || module.isEmpty()) continue;
        		stages.get(module.getModuleType().getIterationStage()).add(module);
        	}
        }

		for (var stage : stages) {
			for (var module : stage) {
        		consumer.accept(module);
			}
		}
	}

	@Override
	public float getMaxAmethystEnergy() {
		float max = 0f;

		for (var module : getModules(ModuleSlot.BINDING)) {
			if (module.getModuleType() instanceof EnergyModule energyModule) {
				max += energyModule.getMaxEnergy(module.getModuleQuality());
			}
		}

		return max;
	}

	@Override
	public float getCurrentAmethystEnergy() {
		float current = 0f;

		for (var module : getModules(ModuleSlot.BINDING)) {
			if (module.getModuleType() instanceof EnergyModule energyModule) {
				current += module.getModuleData().getFloat(EnergyModule.TAG_ENERGY);
			}
		}

		return current;
	}

	@Override
	public void setCurrentAmethystEnergy(float amount) {
		for (var module : getModules(ModuleSlot.BINDING)) {
			if (module.getModuleType() instanceof EnergyModule energyModule) {
				var toSet = Math.min(amount, energyModule.getMaxEnergy(module.getModuleQuality()));
				module.getModuleData().putFloat(EnergyModule.TAG_ENERGY, toSet);
				amount -= toSet;
				if (amount <= 0f) return;
			}
		}
	}
}
