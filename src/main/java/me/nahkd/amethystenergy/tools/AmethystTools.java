package me.nahkd.amethystenergy.tools;

import me.nahkd.amethystenergy.AmethystEnergy;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AmethystTools {
	private static final int AMETHYST_TOOLS_DURABILITY = 350;

	public static final AmethystSword SWORD = new AmethystSword(new FabricItemSettings().maxDamage(AMETHYST_TOOLS_DURABILITY), 3, -2.4f);
	public static final AmethystShovel SHOVEL = new AmethystShovel(new FabricItemSettings().maxDamage(AMETHYST_TOOLS_DURABILITY), 1.5f, -3.0f);
	public static final AmethystPickaxe PICKAXE = new AmethystPickaxe(new FabricItemSettings().maxDamage(AMETHYST_TOOLS_DURABILITY), 1, -2.8f);
	public static final AmethystAxe AXE = new AmethystAxe(new FabricItemSettings().maxDamage(AMETHYST_TOOLS_DURABILITY), 6.0f, -3.1f);
	public static final AmethystHoe HOE = new AmethystHoe(new FabricItemSettings().maxDamage(AMETHYST_TOOLS_DURABILITY), -2, -1.0f);

	private static void register(String id, AmethystTool tool) {
		Registry.register(Registries.ITEM, AmethystEnergy.id(id), (Item) tool);
	}

	public static void registerAll() {
		register("amethyst_sword", SWORD);
		register("amethyst_shovel", SHOVEL);
		register("amethyst_pickaxe", PICKAXE);
		register("amethyst_axe", AXE);
		register("amethyst_hoe", HOE);
	}

	public static void addToGroup(FabricItemGroupEntries entries) {
		entries.add(SWORD);
		entries.add(SHOVEL);
		entries.add(PICKAXE);
		entries.add(AXE);
		entries.add(HOE);
	}
}
