package me.nahkd.amethystenergy.modules;

import java.util.List;
import java.util.function.Consumer;

import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Module extends Item {
	public static final String TAG_MODULE = "Module";
	public static final String TAG_ID = "Id";
	public static final String TAG_QUALITY = "Quality";
	public static final int ITERATION_STAGE_PRE = 0;
	public static final int ITERATION_STAGE_DEFAULT = 1;
	public static final int ITERATION_STAGE_POST = 2;

	public Module(Settings settings) {
		super(settings);
	}

	public abstract ModuleSlot getModuleSlot();
	public abstract void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender);

	public Text getDisplayTextOnTools(int quality, NbtCompound moduleData) {
		if (isCraftableModule()) return getName();
		return Text.empty()
				.append(getName())
				.append(Text.literal(" (").styled(s -> s.withColor(Formatting.DARK_GRAY)))
				.append(Text.literal(quality + "%").styled(s -> s.withColor(Formatting.WHITE)))
				.append(Text.literal(")").styled(s -> s.withColor(Formatting.DARK_GRAY)));
	}

	public boolean destroyOnRemoval() { return true; }
	public boolean isCraftableModule() { return false; } // true if you want the module to be crafted
	public int getIterationStage() { return ITERATION_STAGE_DEFAULT; }
	public ModuleInstance initializeModuleInstance(int quality) { return new ModuleInstance(this, quality); }

	public void onApplyAttributes(ModuleAttributeContext ctx, ModuleInstance instance) {}
	public void onItemDamage(ModuleUseContext ctx, ModuleInstance instance) {}
	public void onAttack(ModuleAttackContext ctx, ModuleInstance instance) {}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		var moduleNbt = stack.getSubNbt(TAG_MODULE);
		var quality = 0;

		if (moduleNbt == null || moduleNbt.isEmpty()) {
			if (isCraftableModule()) {
				quality = 100;
			} else {
				tooltip.add(Text.literal("Broken Module!").styled(s -> s.withColor(Formatting.RED)));
				return;
			}
		} else {
			quality = moduleNbt.getInt(TAG_QUALITY);
		}

		tooltip.add(Text.empty()
				.styled(s -> s.withColor(Formatting.BLUE))
				.append(getModuleSlot().displayText)
				.append(Text.literal(" Module (" + (isCraftableModule()? "Craftable" : quality >= 100? "Perfect" : (quality + "%")) + ")")));
		appendModuleDescription(stack, quality, moduleNbt, text -> {
			tooltip.add(Text.empty().append(text).styled(s -> s.withColor(Formatting.GRAY)));
		});

		if (destroyOnRemoval()) {
			tooltip.add(Text.empty());
			tooltip.add(Text.literal("Destroys when removed from item").styled(s -> s.withColor(Formatting.GRAY)));
		}

		if (context.isCreative() && context.isAdvanced()) {
			tooltip.add(Text.empty());
			tooltip.add(Text.literal("Module Quality: " + quality).styled(s -> s.withColor(Formatting.DARK_GRAY)));
			tooltip.add(Text.literal("Module Iteration Stage: " + getIterationStage()).styled(s -> s.withColor(Formatting.DARK_GRAY)));
		}
	}

	@Override
	public ItemStack getDefaultStack() {
		return Module.createModuleItem(this, 1);
	}

	public Identifier getModuleId() {
		var key = Registries.ITEM.getKey(this).orElse(null);
		return key != null? key.getValue() : new Identifier("missingno");
	}

	public NbtCompound createModuleNbt(int quality) {
		NbtCompound compound = new NbtCompound();
		compound.putString(TAG_ID, getModuleId().toString());
		compound.putInt(TAG_QUALITY, quality);
		return compound;
	}

	public static ItemStack createModuleItem(Module type, int quality) {
		ItemStack stack = new ItemStack(type);
		stack.setSubNbt(TAG_MODULE, type.createModuleNbt(quality));
		return stack;
	}
}
