package me.nahkd.amethystenergy.tools;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Multimap;

import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.modules.ToolUsable;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmethystSword extends SwordItem implements AmethystTool {
	private float attackSpeed;
	private EnumMap<ModuleSlot, Integer> layout;

	public AmethystSword(Settings settings) {
		super(ToolMaterials.IRON, 3, -2.4f, settings);
		this.attackSpeed = -2.4f;
		this.layout = new EnumMap<>(ModuleSlot.class);
		this.layout.put(ModuleSlot.HANDLE, 1);
		this.layout.put(ModuleSlot.BINDING, 1);
		this.layout.put(ModuleSlot.SWORD_BLADE, 1);
	}

	@Override
	public Map<ModuleSlot, Integer> getSlots() {
		return layout;
	}

	@Override
	public float amethystAttackDamage() {
		return getAttackDamage();
	}

	@Override
	public float amethystAttackSpeed() {
		return attackSpeed;
	}

	@Override
	public UUID getAttackDamageModifierId() {
		return ATTACK_DAMAGE_MODIFIER_ID;
	}

	@Override
	public UUID getAttackDamageSpeedId() {
		return ATTACK_SPEED_MODIFIER_ID;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		amethystToolAppendTooltip(stack, world, tooltip, context);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		var out = amethystAttributeModifiers(stack, slot);
		return out != null? out : super.getAttributeModifiers(stack, slot);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		var ctx = new ModuleAttackContext(stack, 1, target, attacker);
		var instance = ctx.getToolInstance();
		instance.forEachModule(module -> module.getModuleType().onAttack(ctx, module));

		if (ctx.durabilityUse > 0) {
			instance.forEachModule(module -> module.getModuleType().onItemDamage(ctx, module));
			stack.damage(ctx.durabilityUse, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0f) {
			var ctx = new ModuleUseContext(stack, 1, miner);
			ctx.getToolInstance().forEachModule(module -> module.getModuleType().onItemDamage(ctx, module)); // TODO: ModuleMineContext
            if (ctx.durabilityUse > 0) stack.damage(ctx.durabilityUse, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }

        return true;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
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

	@Override
	public int getMaxUseTime(ItemStack stack) {
		var instance = new AmethystToolInstance(stack, false);
		var time = 0;

		for (var module : instance.getAllModules()) {
			if (module.getModuleType() instanceof ToolUsable toolModule) {
				time += toolModule.getToolUseTicks(instance, module);
			}
		}

		if (time > 0) return time;
		return super.getMaxUseTime(stack);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		var instance = new AmethystToolInstance(stack, false);

		for (var module : instance.getAllModules()) {
			if (module.getModuleType() instanceof ToolUsable toolModule) {
				toolModule.getToolUseAction(instance, module);
			}
		}

		return super.getUseAction(stack);
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		var ctx = new ModuleUseContext(stack, 0, user);
		var instance = new AmethystToolInstance(stack, true);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof ToolUsable toolModule) toolModule.onUsingTick(ctx, module, remainingUseTicks);
		});

		if (ctx.durabilityUse > 0) stack.damage(ctx.durabilityUse, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		var ctx = new ModuleUseContext(stack, 0, user);
		var instance = new AmethystToolInstance(stack, true);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof ToolUsable toolModule) toolModule.onUsingInterrupt(ctx, module);
		});

		if (ctx.durabilityUse > 0) stack.damage(ctx.durabilityUse, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		var ctx = new ModuleUseContext(stack, 0, user);
		var instance = new AmethystToolInstance(stack, true);
		instance.forEachModule(module -> {
			if (module.getModuleType() instanceof ToolUsable toolModule) toolModule.onUsingFinish(ctx, module);
		});

		if (ctx.durabilityUse > 0) stack.damage(ctx.durabilityUse, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		return stack;
	}
}
