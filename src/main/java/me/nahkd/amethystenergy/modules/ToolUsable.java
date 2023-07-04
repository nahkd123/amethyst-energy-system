package me.nahkd.amethystenergy.modules;

import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import net.minecraft.util.UseAction;

public interface ToolUsable {
	public int getToolUseTicks(AmethystToolInstance tool, ModuleInstance module);
	public UseAction getToolUseAction(AmethystToolInstance tool, ModuleInstance module);

	default void onUsingStart(ModuleUseContext ctx, ModuleInstance instance) {}
	default void onUsingTick(ModuleUseContext ctx, ModuleInstance instance, int ticksLeft) {}
	default void onUsingInterrupt(ModuleUseContext ctx, ModuleInstance instance) {}
	default void onUsingFinish(ModuleUseContext ctx, ModuleInstance instance) {}
}
