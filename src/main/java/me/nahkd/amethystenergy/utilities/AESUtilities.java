package me.nahkd.amethystenergy.utilities;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AESUtilities {
	public static final Item ENERGIZED_AMETHYST = new Item(new FabricItemSettings());

	private static void register(String id, Item item) {
		Registry.register(Registries.ITEM, new Identifier("amethystenergy", id), item);
	}

	public static void registerAll() {
		register("energized_amethyst", ENERGIZED_AMETHYST);
	}

	public static void addToGroup(FabricItemGroupEntries entries) {
		entries.add(ENERGIZED_AMETHYST);
	}
}
