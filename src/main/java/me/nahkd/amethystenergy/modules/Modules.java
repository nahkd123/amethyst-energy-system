package me.nahkd.amethystenergy.modules;

import java.util.ArrayList;
import java.util.List;

import me.nahkd.amethystenergy.AmethystEnergy;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class Modules {
	private static final List<Module> MODULES = new ArrayList<>(); 

	// Templates
	public static final Item TEMPLATE_HANDLE = new Item(new FabricItemSettings());
	public static final Item TEMPLATE_BINDING = new Item(new FabricItemSettings());

	// Modules
	public static final FeatherweightModule FEATHERWEIGHT = new FeatherweightModule();
	public static final MagicDurabilityModule MAGIC_DURABILITY = new MagicDurabilityModule();

	public static final EnergyModule ENERGY = new EnergyModule();

	public static final GlowingBladeModule GLOWING_BLADE = new GlowingBladeModule();
	public static final SoulstealerModule SOULSTEALER = new SoulstealerModule();

	private static void register(String id, Module module) {
		Registry.register(Registries.ITEM, AmethystEnergy.id(id + "_module"), module);
		MODULES.add(module);
	}

	public static void registerAll() {
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(Registries.ITEM_GROUP.getKey(), AmethystEnergy.id("modules"))).register(entries -> {
			entries.add(TEMPLATE_HANDLE);
			entries.add(TEMPLATE_BINDING);

			for (var module : MODULES) entries.add(Module.createModuleItem(module, 100));
		});

		Registry.register(Registries.ITEM, AmethystEnergy.id("handle_module_template"), TEMPLATE_HANDLE);
		Registry.register(Registries.ITEM, AmethystEnergy.id("binding_module_template"), TEMPLATE_BINDING);

		register("featherweight", FEATHERWEIGHT);
		register("magic_durability", MAGIC_DURABILITY);

		register("energy", ENERGY);

		register("glowing_blade", GLOWING_BLADE);
		register("soulstealer", SOULSTEALER);
	}
}
