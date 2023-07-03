package me.nahkd.amethystenergy.tools;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AmethystTools {
	public static final AmethystSword SWORD = new AmethystSword(new FabricItemSettings().maxDamage(350));

	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(SWORD))
			.displayName(Text.literal("Amethyst Energy System - Tools"))
			.build();

	private static void register(String id, AmethystTool tool) {
		Registry.register(Registries.ITEM, new Identifier("amethystenergy", id), tool);
	}

	public static void registerAll() {
		Registry.register(Registries.ITEM_GROUP, new Identifier("amethystenergy", "tools"), ITEM_GROUP);
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier("amethystenergy", "tools"))).register(entries -> {
			entries.add(SWORD);
		});

		register("amethyst_sword", SWORD);
	}
}
