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
	public static final Item TEMPLATE_SWORD_BLADE = new Item(new FabricItemSettings());
	public static final Item TEMPLATE_SHOVEL_HEAD = new Item(new FabricItemSettings());
	public static final Item TEMPLATE_AXE_HEAD = new Item(new FabricItemSettings());
	public static final Item TEMPLATE_PICKAXE_HEAD = new Item(new FabricItemSettings());
	public static final Item TEMPLATE_HOE_BLADE = new Item(new FabricItemSettings());

	// Modules
	public static final FeatherweightModule FEATHERWEIGHT = new FeatherweightModule();
	public static final MagicDurabilityModule MAGIC_DURABILITY = new MagicDurabilityModule();
	public static final MatterCondenserVialModule MATTER_CONDENSER_VIAL = new MatterCondenserVialModule();

	public static final EnergyModule ENERGY = new EnergyModule();

	public static final GlowingBladeModule GLOWING_BLADE = new GlowingBladeModule();
	public static final SoulstealerModule SOULSTEALER = new SoulstealerModule();
	public static final DrillModule DRILL = new DrillModule();
	public static final PickmerangModule PICKMERANG = new PickmerangModule();

	private static void register(String id, Module module) {
		Registry.register(Registries.ITEM, AmethystEnergy.id(id + "_module"), module);
		MODULES.add(module);
	}

	public static void registerAll() {
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(Registries.ITEM_GROUP.getKey(), AmethystEnergy.id("modules"))).register(entries -> {
			entries.add(TEMPLATE_HANDLE);
			entries.add(TEMPLATE_BINDING);
			entries.add(TEMPLATE_SWORD_BLADE);
			entries.add(TEMPLATE_SHOVEL_HEAD);
			entries.add(TEMPLATE_AXE_HEAD);
			entries.add(TEMPLATE_PICKAXE_HEAD);
			entries.add(TEMPLATE_HOE_BLADE);

			for (var module : MODULES) entries.add(Module.createModuleItem(module, 100));
		});

		Registry.register(Registries.ITEM, AmethystEnergy.id("handle_module_template"), TEMPLATE_HANDLE);
		Registry.register(Registries.ITEM, AmethystEnergy.id("binding_module_template"), TEMPLATE_BINDING);
		Registry.register(Registries.ITEM, AmethystEnergy.id("sword_blade_module_template"), TEMPLATE_SWORD_BLADE);
		Registry.register(Registries.ITEM, AmethystEnergy.id("shovel_head_module_template"), TEMPLATE_SHOVEL_HEAD);
		Registry.register(Registries.ITEM, AmethystEnergy.id("axe_head_module_template"), TEMPLATE_AXE_HEAD);
		Registry.register(Registries.ITEM, AmethystEnergy.id("pickaxe_head_module_template"), TEMPLATE_PICKAXE_HEAD);
		Registry.register(Registries.ITEM, AmethystEnergy.id("hoe_blade_module_template"), TEMPLATE_HOE_BLADE);

		register("featherweight", FEATHERWEIGHT);
		register("magic_durability", MAGIC_DURABILITY);
		register("matter_condenser_vial", MATTER_CONDENSER_VIAL);

		register("energy", ENERGY);

		register("glowing_blade", GLOWING_BLADE);
		register("soulstealer", SOULSTEALER);
		register("drill", DRILL);
		register("pickmerang", PICKMERANG);
	}
}
