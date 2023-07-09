package me.nahkd.amethystenergy.modules;

import me.nahkd.amethystenergy.modules.contexts.ModuleMiningModifierContext;

public interface MiningModifier {
	default void onMiningModifier(ModuleMiningModifierContext ctx, ModuleInstance module) {}
}
