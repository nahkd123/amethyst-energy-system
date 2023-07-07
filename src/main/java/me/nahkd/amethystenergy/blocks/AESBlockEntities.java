package me.nahkd.amethystenergy.blocks;

import me.nahkd.amethystenergy.AmethystEnergy;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AESBlockEntities {
	private static void register(String id, BlockEntityType<?> be) {
		Registry.register(Registries.BLOCK_ENTITY_TYPE, AmethystEnergy.id(id), be);
	}

	public static void registerAll() {
	}
}
