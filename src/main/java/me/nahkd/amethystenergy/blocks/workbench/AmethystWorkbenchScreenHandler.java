package me.nahkd.amethystenergy.blocks.workbench;

import java.util.ArrayList;
import java.util.List;

import me.nahkd.amethystenergy.blocks.AESBlockScreenHandlers;
import me.nahkd.amethystenergy.inventory.StackHolder;
import me.nahkd.amethystenergy.modules.EnergyModule;
import me.nahkd.amethystenergy.modules.MatterCondenserVialModule;
import me.nahkd.amethystenergy.modules.Module;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.tools.AmethystTool;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import me.nahkd.amethystenergy.utilities.AESUtilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class AmethystWorkbenchScreenHandler extends ScreenHandler implements Inventory {
	private PlayerInventory playerInventory;

	public final StackHolder shards = new StackHolder();
	public final StackHolder tool = new StackHolder();

	public final List<ModuleSlot> moduleSlotTypes = new ArrayList<>();
	public final List<Integer> moduleSlotIndexes = new ArrayList<>();

	public AmethystWorkbenchScreenHandler(int syncId, PlayerInventory playerInv, PlayerEntity player) {
		super(AESBlockScreenHandlers.AMETHYST_WORKBENCH, syncId); // TODO
		this.playerInventory = playerInv;

		// Block inventory
		addSlot(new Slot(this, 0, 9, 17) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack == null || stack.isEmpty() || stack.getItem() == Items.AMETHYST_SHARD;
			}
		});
		addSlot(new Slot(this, 1, 9, 52) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack == null || stack.isEmpty() || stack.getItem() instanceof AmethystTool;
			}
		});

		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 3; y++) {
				addSlot(new ModuleInventorySlot(this, x + y * 7 + 2, x * 18 + 44, y * 18 + 17));
			}
		}

		// Player's inventory
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 3; y++) {
				addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
		}
	}

	public AmethystWorkbenchScreenHandler(int syncId, PlayerInventory playerInv) {
		this(syncId, playerInv, playerInv.player);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canPlayerUse(player);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		shards.clear();
		tool.clear();
		markDirty();
	}

	@Override
	public ItemStack getStack(int i) {
		if (i == 0) return shards.get();
		if (i == 1) return tool.get();

		if (tool.isEmpty()) return ItemStack.EMPTY;
		var moduleIdx = i - 2;
		if (moduleIdx >= moduleSlotTypes.size()) return ItemStack.EMPTY;
		var toolInstance = new AmethystToolInstance(tool.get(), true);
		var slotType = moduleSlotTypes.get(moduleIdx);
		var slotIndex = moduleSlotIndexes.get(moduleIdx);
		var toolModuleInstance = toolInstance.getModules(slotType).get(slotIndex);
		if (toolModuleInstance.isEmpty()) return ItemStack.EMPTY;

		var moduleData = toolModuleInstance.getModuleData();
		var moduleType = toolModuleInstance.getModuleType();
		var stackOut = new ItemStack(moduleType);
		stackOut.getOrCreateSubNbt(Module.TAG_MODULE).copyFrom(moduleData);
		return stackOut;
		// TODO: Caching
	}

	@Override
	public boolean isEmpty() {
		return shards.isEmpty() && tool.isEmpty();
	}

	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		moduleSlotTypes.clear();
		moduleSlotIndexes.clear();

		if (!tool.isEmpty() && tool.get().getItem() instanceof AmethystTool toolType) {
			for (var e : toolType.getSlots().entrySet()) {
				var type = e.getKey();
				var amount = e.getValue();

				for (int i = 0; i < amount; i++) {
					moduleSlotTypes.add(type);
					moduleSlotIndexes.add(i);
				}
			}
		}
	}

	@Override
	public ItemStack removeStack(int i) {
		ItemStack item;
		if (i == 0) { item = shards.get(); shards.clear(); return item; }
		if (i == 1) { item = tool.get(); tool.clear(); return item; }

		item = getStack(i);
		setStack(i, ItemStack.EMPTY);
		return item;
	}

	@Override
	public ItemStack removeStack(int i, int amount) {
		var old = ItemStack.EMPTY;
		if (i == 0) { old = shards.remove(amount); markDirty(); return old; }
		if (i == 1) { old = tool.remove(amount); markDirty(); return old; }

		old = getStack(i);
		setStack(i, ItemStack.EMPTY);

		markDirty();
		return old;
	}

	@Override
	public void setStack(int i, ItemStack stack) {
		if (i == 0) { shards.set(stack); return; }
		if (i == 1) { tool.set(stack); return; }

		if (shards.isEmpty() || tool.isEmpty()) return;
		var removingModule = stack == null || stack.isEmpty();
		if (!removingModule && !(stack.getItem() instanceof Module)) return;
		if (!removingModule && (!stack.hasNbt() || !stack.getNbt().contains(Module.TAG_MODULE, NbtElement.COMPOUND_TYPE))) return;

		var moduleIdx = i - 2;
		if (moduleIdx >= moduleSlotTypes.size()) return;

		var toolInstance = new AmethystToolInstance(tool.get(), true);
		var slotType = moduleSlotTypes.get(moduleIdx);
		var slotIndex = moduleSlotIndexes.get(moduleIdx);
		var toolModuleInstance = toolInstance.getModules(slotType).get(slotIndex);
		if (removingModule) {
			var keys = toolModuleInstance.getModuleData().getKeys();
			var keysClone = new ArrayList<String>();
			keysClone.addAll(keys);
			for (var key : keysClone) toolModuleInstance.getModuleData().remove(key);
		} else {
			var moduleData = stack.getNbt().getCompound(Module.TAG_MODULE);
			toolModuleInstance.getModuleData().copyFrom(moduleData);
			if (stack.getItem() instanceof Module moduleType) toolModuleInstance.setModuleType(moduleType);
		}

		markDirty();
	}

	@Override
	public int size() {
		return 2; // TODO: include module slots
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIdx) {
		var slot = slots.get(slotIdx);
		var inv = slot.inventory;

		if (inv instanceof PlayerInventory) {
			// Shift clicked from player's inventory
			if (!slot.hasStack()) return ItemStack.EMPTY;
			var stack = slot.getStack();

			if (stack.getItem() == Items.AMETHYST_SHARD) {
				if (shards.insertStack(stack)) slot.setStack(ItemStack.EMPTY);
			}

			if (stack.getItem() instanceof AmethystTool) {
				if (tool.insertStack(stack)) slot.setStack(ItemStack.EMPTY);
			}

			if (stack.getItem() == AESUtilities.ENERGIZED_AMETHYST) {
				if (tool.isEmpty()) return ItemStack.EMPTY;
				var toolInstance = new AmethystToolInstance(tool.get(), true);
				var currentEnergy = toolInstance.getCurrentAmethystEnergy();
				var maxEnergy = toolInstance.getMaxAmethystEnergy();
				var useItems = Math.min((int) Math.floor(maxEnergy - currentEnergy), stack.getCount());
				var newEnergy = Math.min(currentEnergy + useItems, maxEnergy);

				toolInstance.setCurrentAmethystEnergy(newEnergy);
				stack.decrement(useItems);
			}

			if (stack.getItem() instanceof Module moduleType) {
				if (tool.isEmpty()) return ItemStack.EMPTY;
				var toolInstance = new AmethystToolInstance(tool.get(), true);

				if (shards.isEmpty() || shards.get().getCount() < moduleType.shardsApplyCost()) return ItemStack.EMPTY;
				if (moduleType.isAlwaysPerfectModule()) stack.getOrCreateSubNbt(Module.TAG_MODULE).copyFrom(moduleType.createModuleNbt(100));
				if (!stack.hasNbt() || !stack.getNbt().contains(Module.TAG_MODULE, NbtElement.COMPOUND_TYPE)) return ItemStack.EMPTY;

				outer: for (var currentSlotType : ModuleSlot.values()) {
					if (!moduleType.getModuleSlot().compatibleWithModuleSlot(currentSlotType)) continue;
					var modules = toolInstance.getModules(currentSlotType);
					if (modules.size() == 0) continue;

					for (int i = 0; i < modules.size(); i++) {
						var moduleInstance = modules.get(i);

						if (moduleInstance.isEmpty()) {
							moduleInstance.getModuleData().copyFrom(stack.getOrCreateSubNbt(Module.TAG_MODULE));
							stack.decrement(1);
							var shardsStack = shards.get();
							shardsStack.decrement(moduleType.shardsApplyCost());
							shards.set(shardsStack);
							break outer;
						}
					}
				}
			}

			markDirty();
		} else {
			if (!slot.hasStack()) return ItemStack.EMPTY;
			var playerInv = player.getInventory();
			if (playerInv.insertStack(slot.getStack())) slot.setStack(ItemStack.EMPTY);
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		if (slot.getIndex() == 0) return stack != null && !stack.isEmpty() && stack.getItem() == Items.AMETHYST_SHARD;
		if (slot.getIndex() == 1) return stack != null && !stack.isEmpty() && stack.getItem() instanceof AmethystTool;
		return true;
	}

	@Override
	public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canInsertIntoSlot(Slot slot) {
		return slot.getIndex() == 0 || slot.getIndex() == 1;
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex < 0) {
			super.onSlotClick(slotIndex, button, actionType, player);
			return;
		}

		var slot = slots.get(slotIndex);

		if (slot.inventory == this && slot.getIndex() >= 2) {
			var moduleItem = getStack(slot.getIndex());
			var cursorStack = getCursorStack();
			var moduleIdx = slot.getIndex() - 2;
			var slotType = moduleIdx < moduleSlotTypes.size()? moduleSlotTypes.get(moduleIdx) : null;
			var moduleSlotIdx = moduleIdx < moduleSlotIndexes.size()? moduleSlotIndexes.get(moduleIdx) : -1;

			if (moduleItem == null || moduleItem.isEmpty() || !(moduleItem.getItem() instanceof Module moduleType)) {
				if (cursorStack != null && !cursorStack.isEmpty() && cursorStack.getItem() instanceof Module cursorModuleType) {
					if (cursorModuleType.isAlwaysPerfectModule()) cursorStack.getOrCreateSubNbt(Module.TAG_MODULE).copyFrom(cursorModuleType.createModuleNbt(100));
					if (!cursorStack.hasNbt() || !cursorStack.getNbt().contains(Module.TAG_MODULE, NbtElement.COMPOUND_TYPE)) return;
					if (shards.isEmpty() || shards.get().getCount() < cursorModuleType.shardsApplyCost()) return;
					if (slotType != null && !cursorModuleType.getModuleSlot().compatibleWithModuleSlot(slotType)) return;

					super.onSlotClick(slotIndex, button, actionType, player);
					shards.remove(cursorModuleType.shardsApplyCost());
				}

				return;
			}

			if (cursorStack != null && !cursorStack.isEmpty()) {
				var toolInstance = new AmethystToolInstance(tool.get(), true);
				var module = toolInstance.getModules(slotType).get(moduleSlotIdx);
				var moduleData = module.getModuleData();

				if (cursorStack.getItem() == AESUtilities.ENERGIZED_AMETHYST) {
					if (!(moduleItem.getItem() instanceof EnergyModule energyModule)) return;

					// Charge
					var currentEnergy = moduleData.getFloat(EnergyModule.TAG_ENERGY);
					var maxEnergy = energyModule.getMaxEnergy(moduleData.getInt(Module.TAG_QUALITY));
					var useItems = Math.min((int) Math.floor(maxEnergy - currentEnergy), cursorStack.getCount());
					var newEnergy = Math.min(currentEnergy + useItems, maxEnergy);

					moduleData.putFloat(EnergyModule.TAG_ENERGY, newEnergy);
					cursorStack.decrement(useItems);
					return;
				}

				if (cursorStack.getItem() instanceof MatterCondenserVialModule) {
					var quality = module.getModuleQuality();
					if (quality >= 100) return;
					if (!module.getModuleType().canBeUpgraded()) return;

					var extraQuality = player.getRandom().nextBetween(5, 12);
					quality = Math.max(Math.min(quality + extraQuality, 100), 1);

					moduleData.putInt(Module.TAG_QUALITY, quality);
					cursorStack.decrement(1);
					return;
				}
			}

			if (moduleType.destroyOnRemoval()) {
				setStack(slot.getIndex(), ItemStack.EMPTY);
			}

			if (cursorStack != null && !cursorStack.isEmpty() && cursorStack.getItem() instanceof Module cursorModuleType) {
				if (shards.isEmpty() || shards.get().getCount() < cursorModuleType.shardsApplyCost()) return;
				shards.remove(cursorModuleType.shardsApplyCost());
			}
		}

		super.onSlotClick(slotIndex, button, actionType, player);
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		if (!shards.isEmpty()) player.dropItem(shards.get(), true);
		if (!tool.isEmpty()) player.dropItem(tool.get(), true);
	}
}
