package me.nahkd.amethystenergy.tools;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Multimap;

import me.nahkd.amethystenergy.items.HasCustomBars;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class AmethystPickaxe extends PickaxeItem implements AmethystTool, HasCustomBars {
	private float attackSpeed;
	private EnumMap<ModuleSlot, Integer> layout;

	public AmethystPickaxe(Settings settings, int attackDamage, float attackSpeed) {
		super(ToolMaterials.IRON, attackDamage, attackSpeed, settings);
		this.attackSpeed = attackSpeed;
		this.layout = new EnumMap<>(ModuleSlot.class);
		this.layout.put(ModuleSlot.HANDLE, 1);
		this.layout.put(ModuleSlot.BINDING, 1);
		this.layout.put(ModuleSlot.PICKAXE_HEAD, 1);
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
		var ctx = new ModuleAttackContext(stack, 2, target, attacker);
		var instance = ctx.getToolInstance();
		instance.forEachModule(module -> module.getModuleType().onAttack(ctx, module));
		stack.damage(ctx.durabilityUse, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return amethystUse(world, user, hand);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return amethystGetMaxUseTime(stack, super.getMaxUseTime(stack));
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return amethystUseAction(stack, super.getUseAction(stack));
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		amethystUsageTick(world, user, stack, remainingUseTicks);
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		amethystStoppedUsing(stack, world, user, remainingUseTicks);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		return amethystFinishUsing(stack, world, user);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return amethystMiningSpeedMultiplier(super.getMiningSpeedMultiplier(stack, state), stack, state);
	}

	@Override
	public int getBarsCount(ItemStack stack) {
		return amethystGetBarsCount(stack);
	}

	@Override
	public int getBarColor(ItemStack stack, int index) {
		return amethystGetBarColor(stack, index);
	}

	@Override
	public float getBarProgress(ItemStack stack, int index) {
		return amethystGetBarProgress(stack, index);
	}
}
