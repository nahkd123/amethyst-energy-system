package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.AEUtils;
import me.nahkd.amethystenergy.entities.ZhoopEntity;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;

public class GlowingBladeModule extends Module implements ToolUsable {
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
		appender.accept(Text.literal("Hold RMB to charge special attack,"));
		appender.accept(Text.literal("dealing up to " + AEUtils.formatStat(getSpecialAttackDamage(quality)) + " damage for \u26a1 " + AEUtils.formatEnergy(getSpecialAttackEnergyCost(quality)) + "."));
		appender.accept(Text.literal("Does not work if you hold your sword"));
		appender.accept(Text.literal("or something else in offhand slot."));
	}

	public float getBonusDamage(int quality) { return (quality / 100f) * 2f; }
	public float getEnergyCost(int quality) { return quality / 1000f; }

	public float getSpecialAttackDamage(int quality) { return 6f + (quality / 100f) * 12f; }
	public float getSpecialAttackEnergyCost(int quality) { return quality / 20f; }

	@Override
	public void onApplyAttributes(ModuleAttributeContext ctx, ModuleInstance module) {
		ctx.attackDamage += getBonusDamage(module.getModuleQuality());
	}

	@Override
	public void onAttack(ModuleAttackContext ctx, ModuleInstance module) {
		if (!ctx.getToolInstance().useAmethystEnergy(getEnergyCost(module.getModuleQuality()))) ctx.durabilityUse++;
	}

	@Override
	public int getToolUseTicks(AmethystToolInstance tool, ModuleInstance module) {
		return 72000;
	}

	@Override
	public UseAction getToolUseAction(AmethystToolInstance tool, ModuleInstance module) {
		return UseAction.NONE;
	}

	@Override
	public void onUsingTick(ModuleUseContext ctx, ModuleInstance instance, int ticksLeft) {
		var user = ctx.getUser();
		var maxTicks = getToolUseTicks(ctx.getToolInstance(), instance);
		var currentTick = maxTicks - ticksLeft;

		var energyCost = getSpecialAttackEnergyCost(instance.getModuleQuality());
		if (ctx.getToolInstance().getCurrentAmethystEnergy() < energyCost) {
			if (user.getServer() == null && currentTick == 0) user.playSound(SoundEvents.BLOCK_STONE_BREAK, 1f, 1f);
			return;
		}

		if (user.getServer() == null) {
			var pitch = 0.5f + Math.min(currentTick / 60f, 1f) * 0.5f;
			if (currentTick == 60) user.playSound(SoundEvents.BLOCK_BELL_USE, 1f, 2f);
			user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.05f, pitch);
		}
	}

	@Override
	public void onUsingInterrupt(ModuleUseContext ctx, ModuleInstance instance) {
		var user = ctx.getUser();
		if (user.getItemUseTime() < 20) return;

		var hasEnergy = ctx.getToolInstance().useAmethystEnergy(getSpecialAttackEnergyCost(instance.getModuleQuality()));
		if (!hasEnergy) return;

		var charged = Math.min(user.getItemUseTime() / 60f, 1f);
		var damage = getSpecialAttackDamage(instance.getModuleQuality()) * charged;

		if (user.getServer() == null) {
			user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
			user.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, 1f, 2f);
			user.swingHand(user.getActiveHand());
		} else {
			var zhoop = new ZhoopEntity(user.getWorld(), user);
			zhoop.setVelocity(user, user.getPitch(), user.getYaw(), 0f, 1.5f, 0f);
			zhoop.setBladeDamage(damage);
			user.getWorld().spawnEntity(zhoop);
		}

		ctx.durabilityUse++;
	}
}
