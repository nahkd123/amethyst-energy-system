package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class MagicDurabilityModule extends Module {
	public MagicDurabilityModule() {
		super(new FabricItemSettings().maxCount(1));
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.HANDLE;
	}

	@Override
	public boolean isAlwaysPerfectModule() {
		return true;
	}

	@Override
	public int getIterationPriority() {
		return PRIORITY_LATEST;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		appender.accept(Text.literal("Redirect durability usage to energy"));
		appender.accept(Text.literal("module at " + EnergyModule.SYMBOL + " 0.20 for each durability"));
		appender.accept(Text.literal("point."));
	}

	@Override
	public void onItemDamage(ModuleUseContext ctx, ModuleInstance module) {
		var instance = ctx.getToolInstance();

		while (ctx.durabilityUse > 0) {
			if (instance.useAmethystEnergy(0.2f)) {
				ctx.durabilityUse--;
			} else {
				break;
			}
		}
	}
}
