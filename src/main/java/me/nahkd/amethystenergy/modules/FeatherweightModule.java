package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class FeatherweightModule extends Module {
	public FeatherweightModule() {
		super(new FabricItemSettings());
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.HANDLE;
	}

	public float getStat(int quality) {
		return (quality / 100f) * 0.5f;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		appender.accept(Text.literal("Make your item " + AEUtils.formatPercentage(getStat(quality)) + " lighter"));
	}

	@Override
	public void onApplyAttributes(ModuleAttributeContext ctx, NbtCompound moduleData, int quality) {
		ctx.attackSpeed /= 1f + (quality / 100f) * 0.5f;
	}
}
