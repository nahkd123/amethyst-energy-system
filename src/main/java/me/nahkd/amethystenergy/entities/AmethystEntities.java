package me.nahkd.amethystenergy.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AmethystEntities {
	public static final EntityType<ZhoopEntity> ZHOOP = FabricEntityTypeBuilder.<ZhoopEntity>create(SpawnGroup.MISC, ZhoopEntity::new)
			.dimensions(EntityDimensions.fixed(0.25f, 0.25f))
			.trackRangeBlocks(4).trackedUpdateRate(10)
			.build();

	public static void registerAll() {
		Registry.register(Registries.ENTITY_TYPE, new Identifier("amethystenergy", "zhoop"), ZHOOP);
	}
}