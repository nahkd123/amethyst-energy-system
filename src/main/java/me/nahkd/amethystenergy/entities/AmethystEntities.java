package me.nahkd.amethystenergy.entities;

import me.nahkd.amethystenergy.AmethystEnergy;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AmethystEntities {
	public static final EntityType<ZhoopEntity> ZHOOP = FabricEntityTypeBuilder.<ZhoopEntity>create(SpawnGroup.MISC, ZhoopEntity::new)
			.dimensions(EntityDimensions.fixed(0.7f, 0.15f))
			.trackRangeBlocks(4).trackedUpdateRate(10)
			.build();

	public static void registerAll() {
		Registry.register(Registries.ENTITY_TYPE, AmethystEnergy.id("zhoop"), ZHOOP);
	}
}
