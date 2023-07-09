package me.nahkd.amethystenergy.tools;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.modules.MiningModifier;
import me.nahkd.amethystenergy.modules.ToolUsable;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleMiningModifierContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public interface AmethystTool {
	public static final String TAG_MODULES = "Modules";

	public Map<ModuleSlot, Integer> getSlots();
	public float amethystAttackDamage();
	public float amethystAttackSpeed();
	public UUID getAttackDamageModifierId();
	public UUID getAttackDamageSpeedId();

	default NbtCompound createEmptySlots() {
		var nbt = new NbtCompound();
		for (var slotType : getSlots().keySet()) {
			var amount = getSlots().get(slotType);
			if (amount == null || amount <= 0) continue;

			var list = new NbtList();
			for (int i = 0; i < amount; i++) list.add(new NbtCompound());
			nbt.put(slotType.slotName, list);
		}
		return nbt;
	}

	default void amethystToolAppendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		AmethystToolInstance instance = new AmethystToolInstance(stack, false);

		tooltip.add(Text.literal("Modules:").styled(s -> s.withColor(Formatting.BLUE)));
		for (var e : getSlots().entrySet()) {
			var slot = e.getKey();
			var amount = e.getValue();
			var modules = instance.getModules(slot);
			if (amount == null || amount == 0) continue;

			if (amount == 1) {
				var module = modules.size() == 1? modules.get(0) : null;
				var moduleType = module != null? module.getModuleType() : null;
				var moduleQuality = module != null? module.getModuleQuality() : -1;

				tooltip.add(Text.literal("  ")
						.styled(s -> s.withColor(Formatting.GRAY))
						.append(Text.empty().append(slot.toolDisplayText).styled(s -> s.withColor(Formatting.BLUE)))
						.append(": ")
						.append((module != null && !module.isEmpty())? moduleType.getDisplayTextOnTools(moduleQuality, module.getModuleData()) : Text.literal("(none)").styled(s -> s.withItalic(true))));
			} else {
				tooltip.add(Text.literal("  ")
						.styled(s -> s.withColor(Formatting.GRAY))
						.append(Text.empty().append(slot.toolDisplayText).styled(s -> s.withColor(Formatting.BLUE)))
						.append(":"));

				for (int i = 0; i < amount; i++) {
					var module = i < modules.size()? modules.get(i) : null;
					var moduleType = module != null? module.getModuleType() : null;
					var moduleQuality = module != null? module.getModuleQuality() : -1;

					tooltip.add(Text.literal("    ")
							.styled(s -> s.withColor(Formatting.GRAY))
							.append(Text.literal((i + 1) + ". ").styled(s -> s.withColor(Formatting.BLUE)))
							.append((module != null && !module.isEmpty())? moduleType.getDisplayTextOnTools(moduleQuality, module.getModuleData()) : Text.literal("(none)").styled(s -> s.withItalic(true))));
				}
			}
		}
	}

	default Multimap<EntityAttribute, EntityAttributeModifier> amethystAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
        	var ctx = new ModuleAttributeContext(stack, amethystAttackDamage(), amethystAttackSpeed());

            if (stack.hasNbt() && stack.getNbt().contains(TAG_MODULES, NbtElement.COMPOUND_TYPE)) {
            	var instance = ctx.getToolInstance();
            	instance.forEachModule(module -> module.getModuleType().onApplyAttributes(ctx, module));
            }

        	ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(getAttackDamageModifierId(), "Weapon modifier", ctx.attackDamage, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(getAttackDamageSpeedId(), "Weapon modifier", ctx.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return null;
    }

	default TypedActionResult<ItemStack> amethystUse(World world, PlayerEntity user, Hand hand) {
		var using = user.getStackInHand(hand);
		var ctx = new ModuleUseContext(using, 0, user);
		var instance = ctx.getToolInstance();
		var found = false;

		for (var module : instance.getAllModules()) {
			if (module.getModuleType() instanceof ToolUsable toolModule) {
				found = true;
				toolModule.onUsingStart(ctx, module);
			}
		}

		if (found) {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(using);
		}

		return TypedActionResult.fail(using);
	}

	default int amethystGetMaxUseTime(ItemStack stack, int def) {
		var instance = new AmethystToolInstance(stack, false);
		var time = 0;

		for (var module : instance.getAllModules()) {
			if (module.getModuleType() instanceof ToolUsable toolModule) {
				time += toolModule.getToolUseTicks(instance, module);
			}
		}

		if (time > 0) return time;
		return def;
	}

	default UseAction amethystUseAction(ItemStack stack, UseAction def) {
		var instance = new AmethystToolInstance(stack, false);

		for (var module : instance.getAllModules()) {
			if (module.getModuleType() instanceof ToolUsable toolModule) {
				toolModule.getToolUseAction(instance, module);
			}
		}

		return def;
	}

	default void amethystUsageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		var ctx = new ModuleUseContext(stack, 0, user);
		var instance = new AmethystToolInstance(stack, true);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof ToolUsable toolModule) toolModule.onUsingTick(ctx, module, remainingUseTicks);
		});

		stack.damage(ctx.durabilityUse, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
	}

	default void amethystStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		var ctx = new ModuleUseContext(stack, 0, user);
		var instance = new AmethystToolInstance(stack, true);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof ToolUsable toolModule) toolModule.onUsingInterrupt(ctx, module);
		});

		stack.damage(ctx.durabilityUse, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
	}

	default ItemStack amethystFinishUsing(ItemStack stack, World world, LivingEntity user) {
		var ctx = new ModuleUseContext(stack, 0, user);
		var instance = new AmethystToolInstance(stack, true);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof ToolUsable toolModule) toolModule.onUsingFinish(ctx, module);
		});

		stack.damage(ctx.durabilityUse, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		return stack;
	}

	default float amethystMiningSpeedMultiplier(float def, ItemStack stack, BlockState state) {
		var ctx = new ModuleMiningModifierContext(stack, def);
		var instance = new AmethystToolInstance(stack, false);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof MiningModifier miningMod) miningMod.onMiningModifier(ctx, module);
		});

		return ctx.miningSpeed;
	}
}
