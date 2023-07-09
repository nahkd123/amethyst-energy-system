package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import me.nahkd.amethystenergy.modules.contexts.ModuleMiningModifierContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;

public class DrillModule extends Module implements MiningModifier, ToolUsable {
	private static final String TAG_ACTIVATED = "Activated";

	public DrillModule() {
		super(new FabricItemSettings().maxCount(1));
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.DIGGING_HEAD;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		appender.accept(Text.literal(AEUtils.formatStat(getDrillSpeed(quality)) + "x your mining speed."));
		appender.accept(Text.literal("Cost " + EnergyModule.SYMBOL + " " + AEUtils.formatEnergy(getDrillEnergyUsage(quality)) + " for each block mined."));
		appender.accept(Text.literal("RMB to activate/deactivate."));
	}

	public float getDrillSpeed(int quality) { return 1 + quality / 100f * 19f; }
	public float getDrillEnergyUsage(int quality) { return (2f - (quality / 100f)) * 0.12f; }

	@Override
	public void onMiningModifier(ModuleMiningModifierContext ctx, ModuleInstance module) {
		if (!module.getModuleData().getBoolean(TAG_ACTIVATED)) return;

		if (ctx.getToolInstance().getCurrentAmethystEnergy() > getDrillEnergyUsage(module.getModuleQuality())) {
			ctx.miningSpeed *= getDrillSpeed(module.getModuleQuality());
		}
	}

	@Override
	public void onItemDamage(ModuleUseContext ctx, ModuleInstance module) {
		if (!module.getModuleData().getBoolean(TAG_ACTIVATED)) return;
		ctx.getToolInstance().useAmethystEnergy(ctx.durabilityUse * getDrillEnergyUsage(module.getModuleQuality()));
	}

	@Override
	public int getToolUseTicks(AmethystToolInstance tool, ModuleInstance module) {
		return 0;
	}

	@Override
	public UseAction getToolUseAction(AmethystToolInstance tool, ModuleInstance module) {
		return UseAction.NONE;
	}

	@Override
	public void onUsingStart(ModuleUseContext ctx, ModuleInstance instance) {
		boolean state = instance.getModuleData().getBoolean(TAG_ACTIVATED);
		state = !state;
		instance.getModuleData().putBoolean(TAG_ACTIVATED, state);

		if (ctx.getUser() instanceof ServerPlayerEntity player) {
			player.sendMessage(Text.literal("Drill ").append(getActivationState(instance.getModuleData())), true);
		} else if (ctx.getUser().getServer() == null) {
			ctx.getUser().getWorld().playSound(ctx.getUser(), ctx.getUser().getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, state? 1f : 0.5f);
		}
	}

	private Text getActivationState(NbtCompound moduleData) {
		final var state = moduleData.getBoolean(TAG_ACTIVATED);
		return Text.literal(state? "ACTIVATED" : "DEACTIVATED").styled(s -> s.withColor(state? Formatting.GREEN : Formatting.RED));
	}

	@Override
	public Text getDisplayTextOnTools(int quality, NbtCompound moduleData) {
		return Text.empty().append(super.getDisplayTextOnTools(quality, moduleData)).append(" ").append(getActivationState(moduleData));
	}
}
