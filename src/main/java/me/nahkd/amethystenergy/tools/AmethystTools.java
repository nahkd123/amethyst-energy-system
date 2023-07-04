package me.nahkd.amethystenergy.tools;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AmethystTools {
	public static final AmethystSword SWORD = new AmethystSword(new FabricItemSettings().maxDamage(350));

	private static void register(String id, AmethystTool tool) {
		Registry.register(Registries.ITEM, new Identifier("amethystenergy", id), (Item) tool);
	}

	public static void registerAll() {
		register("amethyst_sword", SWORD);
	}

	public static void addToGroup(FabricItemGroupEntries entries) {
		entries.add(SWORD);
	}
}
