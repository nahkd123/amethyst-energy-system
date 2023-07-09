package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SoulstealerModule extends Module {
	public SoulstealerModule() {
		super(new FabricItemSettings().maxCount(1));
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.HOE_BLADE;
	}

	@Override
	public boolean destroyOnRemoval() {
		return true;
	}

	public float getBonusDamage(int quality) { return (quality / 100f) * 6f; }
	public float getBonusAttackSpeed(int quality) { return quality / 100f; }

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		appender.accept(Text.literal("A blade for the challengers."));
		appender.accept(Text.empty());
		appender.accept(Text.literal("Bonus damage: " + AEUtils.formatStat(getBonusDamage(quality))));
		appender.accept(Text.literal("Bonus attack speed: " + AEUtils.formatStat(getBonusAttackSpeed(quality))));
		appender.accept(Text.empty());
		appender.accept(Text.literal("Beware! Using this module will"));
		appender.accept(Text.literal("damage your hoe faster than"));
		appender.accept(Text.literal("usual."));
		appender.accept(Text.empty());

		if (quality < 100) {
			appender.accept(Text.literal("Accept Purpris'h Challenges to increase"));
			appender.accept(Text.literal("your blade quality."));
		} else {
			appender.accept(Text.literal("Maximum quality reached!").styled(s -> s.withColor(Formatting.AQUA)));
		}
	}

	@Override
	public void onApplyAttributes(ModuleAttributeContext ctx, ModuleInstance instance) {
		ctx.attackDamage += getBonusDamage(instance.getModuleQuality());
		ctx.attackSpeed += getBonusAttackSpeed(instance.getModuleQuality());
	}

	@Override
	public void onAttack(ModuleAttackContext ctx, ModuleInstance instance) {
		ctx.durabilityUse *= 2;
	}
}
