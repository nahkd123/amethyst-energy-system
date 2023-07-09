package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EnergyModule extends Module {
	public static final String TAG_ENERGY = "Energy";
	public static final char SYMBOL = '\u26a1';

	public EnergyModule() {
		super(new FabricItemSettings().maxCount(1));
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.BINDING;
	}

	@Override
	public int getMaximumIdentifyQuality() {
		return 25;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		var energy = moduleData.getFloat(TAG_ENERGY);
		appender.accept(Text.literal("Store amethyst energy (AE) for your tool"));
		appender.accept(Text.literal("Current energy: ")
				.append(Text.literal(SYMBOL + " ")
						.styled(s -> s.withColor(Formatting.DARK_PURPLE))
						.append(Text.literal(AEUtils.formatEnergy(energy)).styled(s -> s.withColor(Formatting.LIGHT_PURPLE)))
						.append("/")
						.append(Text.literal(AEUtils.formatEnergy(getMaxEnergy(quality)) + " AE").styled(s -> s.withColor(Formatting.LIGHT_PURPLE))))
				);
		appender.accept(Text.empty());
		appender.accept(Text.literal("Click on this module with Energized Amethyst"));
		appender.accept(Text.literal("on your cursor in Amethyst Workbench"));
		appender.accept(Text.literal("to charge."));
	}

	public float getMaxEnergy(int quality) {
		return (quality / 100f) * 256f;
	}

	@Override
	public Text getDisplayTextOnTools(int quality, NbtCompound moduleData) {
		var energy = moduleData.getFloat(TAG_ENERGY);
		return Text.literal(SYMBOL + " ")
				.styled(s -> s.withColor(Formatting.DARK_PURPLE))
				.append(Text.literal(AEUtils.formatEnergy(energy)).styled(s -> s.withColor(Formatting.LIGHT_PURPLE)))
				.append("/")
				.append(Text.literal(AEUtils.formatEnergy(getMaxEnergy(quality)) + " AE").styled(s -> s.withColor(Formatting.LIGHT_PURPLE)));
	}

	@Override
	public boolean destroyOnRemoval() {
		return false;
	}

	@Override
	public NbtCompound createModuleNbt(int quality) {
		var nbt = super.createModuleNbt(quality);
		nbt.putFloat(TAG_ENERGY, 0f);
		return nbt;
	}
}
