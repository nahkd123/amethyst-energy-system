package me.nahkd.amethystenergy.tools;

import me.nahkd.amethystenergy.AmethystEnergy;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AmethystTools {
	public static final AmethystSword SWORD = new AmethystSword(new FabricItemSettings().maxDamage(350));
	public static final AmethystHoe HOE = new AmethystHoe(new FabricItemSettings().maxDamage(350));

	private static void register(String id, AmethystTool tool) {
		Registry.register(Registries.ITEM, AmethystEnergy.id(id), (Item) tool);
	}

	public static void registerAll() {
		register("amethyst_sword", SWORD);
		register("amethyst_hoe", HOE);
	}

	public static void addToGroup(FabricItemGroupEntries entries) {
		entries.add(SWORD);
		entries.add(HOE);
	}
}
