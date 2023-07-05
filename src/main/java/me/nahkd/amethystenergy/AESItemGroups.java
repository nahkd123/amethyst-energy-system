package me.nahkd.amethystenergy;

import me.nahkd.amethystenergy.blocks.AESBlocks;
import me.nahkd.amethystenergy.modules.Modules;
import me.nahkd.amethystenergy.tools.AmethystTools;
import me.nahkd.amethystenergy.utilities.AESUtilities;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AESItemGroups {
	public static final ItemGroup UNIVERSAL = FabricItemGroup.builder()
			.icon(() -> new ItemStack(AmethystTools.SWORD))
			.displayName(Text.literal("Amethyst Energy System - Universal"))
			.build();

	public static final ItemGroup MODULES = FabricItemGroup.builder()
			.icon(() -> new ItemStack(Modules.TEMPLATE_HANDLE))
			.displayName(Text.literal("Amethyst Energy System - Modules"))
			.build();

	public static void registerAll() {
		Registry.register(Registries.ITEM_GROUP, new Identifier("amethystenergy", "universal"), AESItemGroups.UNIVERSAL);
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier("amethystenergy", "universal"))).register(entries -> {
			AESBlocks.addToGroup(entries);
			AmethystTools.addToGroup(entries);
			AESUtilities.addToGroup(entries);
		});

		Registry.register(Registries.ITEM_GROUP, new Identifier("amethystenergy", "modules"), AESItemGroups.MODULES);
	}
}
