package me.nahkd.amethystenergy.blocks;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AESBlockEntities {
	private static void register(String id, BlockEntityType<?> be) {
		Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("amethystenergy", id), be);
	}

	public static void registerAll() {
	}
}
