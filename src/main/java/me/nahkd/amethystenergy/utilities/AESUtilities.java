package me.nahkd.amethystenergy.utilities;

import me.nahkd.amethystenergy.AmethystEnergy;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AESUtilities {
	public static final Item ENERGIZED_AMETHYST = new Item(new FabricItemSettings());

	private static void register(String id, Item item) {
		Registry.register(Registries.ITEM, AmethystEnergy.id(id), item);
	}

	public static void registerAll() {
		register("energized_amethyst", ENERGIZED_AMETHYST);
	}

	public static void addToGroup(FabricItemGroupEntries entries) {
		entries.add(ENERGIZED_AMETHYST);
	}
}
