package me.nahkd.amethystenergy.modules;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Modules {
	private static final List<Module> MODULES = new ArrayList<>(); 

	// Templates
	public static final Item TEMPLATE_HANDLE = new Item(new FabricItemSettings());
	public static final Item TEMPLATE_BINDING = new Item(new FabricItemSettings());

	// Group
	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(TEMPLATE_HANDLE))
			.displayName(Text.literal("Amethyst Energy System - Modules"))
			.build();

	// Modules
	public static final FeatherweightModule FEATHERWEIGHT = new FeatherweightModule();
	public static final MagicDurabilityModule MAGIC_DURABILITY = new MagicDurabilityModule();

	public static final EnergyModule ENERGY = new EnergyModule();

	public static final GlowingBladeModule GLOWING_BLADE = new GlowingBladeModule();

	private static void register(String id, Module module) {
		Registry.register(Registries.ITEM, new Identifier("amethystenergy", id + "_module"), module);
		MODULES.add(module);
	}

	public static void registerAll() {
		Registry.register(Registries.ITEM_GROUP, new Identifier("amethystenergy", "modules"), ITEM_GROUP);
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier("amethystenergy", "modules"))).register(entries -> {
			entries.add(TEMPLATE_HANDLE);
			entries.add(TEMPLATE_BINDING);

			for (var module : MODULES) entries.add(Module.createModuleItem(module, 100));
		});

		Registry.register(Registries.ITEM, new Identifier("amethystenergy", "handle_module_template"), TEMPLATE_HANDLE);
		Registry.register(Registries.ITEM, new Identifier("amethystenergy", "binding_module_template"), TEMPLATE_BINDING);

		register("featherweight", FEATHERWEIGHT);
		register("magic_durability", MAGIC_DURABILITY);

		register("energy", ENERGY);

		register("glowing_blade", GLOWING_BLADE);
	}
}
