package me.nahkd.amethystenergy.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class AESBlocks {
	public static final Block CONDENSED_AMETHYST_BLOCK = new Block(FabricBlockSettings.create()
			.mapColor(DyeColor.PURPLE)
			.strength(1.5f)
			.sounds(BlockSoundGroup.AMETHYST_BLOCK)
			.requiresTool());

	private static void register(String id, Block block) {
		Registry.register(Registries.BLOCK, new Identifier("amethystenergy", id), block);
		Registry.register(Registries.ITEM, new Identifier("amethystenergy", id), new BlockItem(block, new FabricItemSettings()));
	}

	public static void registerAll() {
		register("condensed_amethyst_block", CONDENSED_AMETHYST_BLOCK);
	}

	public static void addToGroup(FabricItemGroupEntries entries) {
		entries.add(CONDENSED_AMETHYST_BLOCK);
	}
}
