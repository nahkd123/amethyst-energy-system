package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class GlowingBladeModule extends Module {
	public GlowingBladeModule() {
		super(new FabricItemSettings());
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.SWORD_BLADE;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		appender.accept(Text.literal("A blade of legends."));
		appender.accept(Text.empty());
		appender.accept(Text.literal("When used properly, this blade module"));
		appender.accept(Text.literal("deals " + AEUtils.formatStat(getBonusDamage(quality)) + " extra damage at a cost of"));
		appender.accept(Text.literal("\u26a1 " + AEUtils.formatEnergy(getEnergyCost(quality)) + " or 1 extra durability point."));
		appender.accept(Text.empty());
		appender.accept(Text.literal("Holding RMB without shield in offhand"));
		appender.accept(Text.literal("allows you to perform special attack,"));
		appender.accept(Text.literal("dealing " + AEUtils.formatStat(getSpecialAttackDamage(quality)) + " damage for \u26a1 " + AEUtils.formatEnergy(getSpecialAttackEnergyCost(quality))));
	}

	public float getBonusDamage(int quality) { return (quality / 100f) * 2f; }
	public float getEnergyCost(int quality) { return quality / 1000f; }

	public float getSpecialAttackDamage(int quality) { return 6f + (quality / 100f) * 9f; }
	public float getSpecialAttackEnergyCost(int quality) { return quality / 20f; }

	@Override
	public void onApplyAttributes(ModuleAttributeContext ctx, NbtCompound moduleData, int quality) {
		ctx.attackDamage += getBonusDamage(quality);
	}

	@Override
	public void onAttack(ModuleAttackContext ctx, NbtCompound moduleData, int quality) {
		if (!ctx.addEnergyCost(getEnergyCost(quality))) ctx.durabilityUse++;
	}

	// TODO: Implement hold to attack mechanic
}
