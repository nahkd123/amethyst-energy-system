package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import me.nahkd.amethystenergy.blocks.AESBlocks;
import me.nahkd.amethystenergy.items.HasCustomBars;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MatterCondenserVialModule extends Module implements HasCustomBars {
	public static final char SYMBOL = '\u2bcc';
	public static final String TAG_CONDENSED = "Condensed";
	public static final int CONDENSED_BAR_COLOR = 0x55FFFF;

	public MatterCondenserVialModule() {
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
	public boolean destroyOnRemoval() {
		return false;
	}

	@Override
	public int getIterationPriority() {
		return priorityBetween(PRIORITY_DEFAULT, Modules.MAGIC_DURABILITY.getIterationPriority());
	}

	@Override
	public NbtCompound createModuleNbt(int quality) {
		var nbt = super.createModuleNbt(quality);
		nbt.putFloat(TAG_CONDENSED, 0f);
		return nbt;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		var condensed = moduleData.getFloat(TAG_CONDENSED);

		appender.accept(Text.literal("\"Rewards only grants for those who").styled(s -> s.withItalic(true)));
		appender.accept(Text.literal("have patience\"").styled(s -> s.withItalic(true)));
		appender.accept(Text.empty());
		appender.accept(Text.literal("Allow you to condense energy from"));
		appender.accept(Text.literal("amethyst dust into a vial."));
		appender.accept(Text.empty());

		if (condensed < 1f) {
			appender.accept(Text.literal("Current progress: ").append(getDisplayTextOnTools(quality, moduleData)));
		} else {
			appender.accept(Text.literal("Your capsule is maxed!"));
			appender.accept(Text.literal("Open ").append(AESBlocks.AMETHYST_WORKBENCH.getName()).append(", put your tool"));
			appender.accept(Text.literal("and click on module with this capsule to"));
			appender.accept(Text.literal("upgrade its quality by 5 to 12%!"));
			appender.accept(Text.literal("Some modules can't be upgraded this way."));
		}
	}

	@Override
	public Text getDisplayTextOnTools(int quality, NbtCompound moduleData) {
		var condensed = moduleData.getFloat(TAG_CONDENSED);

		return Text.literal(SYMBOL + " ").styled(s -> s.withColor(Formatting.DARK_PURPLE))
				.append(Text.literal(AEUtils.formatPercentage(condensed)).styled(s -> s.withColor(Formatting.LIGHT_PURPLE)));
	}

	@Override
	public void onItemDamage(ModuleUseContext ctx, ModuleInstance instance) {
		var condensed = instance.getModuleData().getFloat(TAG_CONDENSED);
		if (condensed >= 1f) return;

		for (int i = 0; i < ctx.durabilityUse; i++) {
			var rolled = ctx.getUser().getRandom().nextFloat();
			if (rolled < 0.1f) condensed += ctx.getUser().getRandom().nextFloat() * 0.01f;
		}

		instance.getModuleData().putFloat(TAG_CONDENSED, Math.max(Math.min(condensed, 1f), 0f));
	}

	@Override
	public int getBarsCount(ItemStack stack) {
		return 1;
	}

	@Override
	public int getBarColor(ItemStack stack, int index) {
		return CONDENSED_BAR_COLOR;
	}

	@Override
	public float getBarProgress(ItemStack stack, int index) {
		if (!stack.hasNbt() || !stack.getNbt().contains(TAG_MODULE, NbtElement.COMPOUND_TYPE)) return 0f;
		var moduleData = stack.getSubNbt(TAG_MODULE);
		return Math.max(Math.min(moduleData.getFloat(TAG_CONDENSED), 1f), 0f);
	}
}
