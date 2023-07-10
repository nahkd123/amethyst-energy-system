package me.nahkd.amethystenergy.modules;

import java.util.function.Consumer;

import me.nahkd.amethystenergy.entities.BoomerangEntity;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import me.nahkd.amethystenergy.tools.AmethystToolInstance;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;

public class PickmerangModule extends Module implements ToolUsable {
	private static final int HOLD_TICKS = 20;

	public PickmerangModule() {
		super(new FabricItemSettings().maxCount(1));
	}

	@Override
	public ModuleSlot getModuleSlot() {
		return ModuleSlot.PICKAXE_HEAD;
	}

	@Override
	public boolean isAlwaysPerfectModule() {
		return true;
	}

	@Override
	public void appendModuleDescription(ItemStack stack, int quality, NbtCompound moduleData, Consumer<Text> appender) {
		appender.accept(Text.literal("Turn your pickaxe into a boomerang!"));
	}

	@Override
	public int getToolUseTicks(AmethystToolInstance tool, ModuleInstance module) {
		return 72000;
	}

	@Override
	public UseAction getToolUseAction(AmethystToolInstance tool, ModuleInstance module) {
		return UseAction.BLOCK;
	}

	@Override
	public void onUsingInterrupt(ModuleUseContext ctx, ModuleInstance instance) {
		var ticks = ctx.getUser().getItemUseTime();
		if (ticks < HOLD_TICKS) return;

		var user = ctx.getUser();
		var newStack = ctx.getItemStack().copyAndEmpty();
		newStack.damage(1, ctx.getUser(), e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));

		var fi = new BoomerangEntity(user.getWorld(), user);
		fi.setStack(newStack);
		fi.setDamage(4f + Math.min((ticks - HOLD_TICKS) / 20f, 3f));
		user.getWorld().spawnEntity(fi);

		if (user.getServer() == null) {
			user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
		}
	}
}
