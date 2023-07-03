package me.nahkd.amethystenergy.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import me.nahkd.amethystenergy.modules.ForEachModule;
import me.nahkd.amethystenergy.modules.Module;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public abstract class AmethystTool extends ToolItem {
	public static final String TAG_MODULES = "Modules";

	private float baseAttackDamage, baseAttackSpeed;
	private List<ModuleSlot> slotsLayout;

	public AmethystTool(Settings settings, List<ModuleSlot> slotsLayout, float attackDamage, float attackSpeed) {
		super(ToolMaterials.IRON, settings);
		this.slotsLayout = Collections.unmodifiableList(new ArrayList<>(slotsLayout));
        baseAttackDamage = attackDamage + getMaterial().getAttackDamage();
        baseAttackSpeed = attackSpeed;
	}

	public List<ModuleSlot> getSlotsLayout() {
		return slotsLayout;
	}

	public NbtCompound getFirstModule(NbtList list, ModuleSlot slot) {
		if (list == null) return new NbtCompound();
		var i = slotsLayout.indexOf(slot);
		if (i < list.size()) return list.getCompound(i);
		return new NbtCompound();
	}

	public NbtList createEmptyLayout() {
		var list = new NbtList();
		for (var i = 0; i < slotsLayout.size(); i++) list.add(new NbtCompound());
		return list;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		NbtList modules = stack.getNbt() != null? stack.getNbt().getList(TAG_MODULES, NbtList.COMPOUND_TYPE) : null;

		tooltip.add(Text.literal("Modules:").styled(s -> s.withColor(Formatting.BLUE)));
		for (int i = 0; i < slotsLayout.size(); i++) {
			var type = slotsLayout.get(i);
			var module = (modules != null && i < modules.size() && modules.get(i) instanceof NbtCompound c && !c.isEmpty())? c : null;
			var moduleType = Module.getModuleType(module);
			var moduleQuality = Module.getModuleQuality(module);

			tooltip.add(Text.literal("    ")
					.styled(s -> s.withColor(Formatting.GRAY))
					.append(Text.empty().append(type.toolDisplayText).styled(s -> s.withColor(Formatting.BLUE)))
					.append(": ")
					.append(module != null? moduleType.getDisplayTextOnTools(moduleQuality, module) : Text.literal("(none)").styled(s -> s.withItalic(true)))
					);
		}
	}

	@Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
        	var ctx = new ModuleAttributeContext(stack, baseAttackDamage, baseAttackSpeed);

            if (stack.hasNbt() && stack.getNbt().contains(TAG_MODULES, NbtElement.LIST_TYPE)) {
            	var modules = stack.getNbt().getList(TAG_MODULES, NbtElement.COMPOUND_TYPE);

            	for (int i = 0; i < modules.size(); i++) {
            		var module = modules.getCompound(i);
            		if (module == null || module.isEmpty()) continue;
            		var moduleType = Module.getModuleType(module);
            		var moduleQuality = Module.getModuleQuality(module);
            		moduleType.onApplyAttributes(ctx, module, moduleQuality);
            	}
            }

        	ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", ctx.attackDamage, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", ctx.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slot);
    }

	public float getAttackDamage() {
		// TODO: Attack damage from modules
		return baseAttackDamage;
	}

	public static void forEachModules(ItemStack stack, ForEachModule consumer) {
		List<List<NbtCompound>> stages = new ArrayList<>();
		for (int i = 0; i < 3; i++) stages.add(new ArrayList<>());

		if (stack.hasNbt() && stack.getNbt().contains(TAG_MODULES, NbtElement.LIST_TYPE)) {
        	var modules = stack.getNbt().getList(TAG_MODULES, NbtElement.COMPOUND_TYPE);

        	for (int i = 0; i < modules.size(); i++) {
        		var module = modules.getCompound(i);
        		if (module == null || module.isEmpty()) continue;
        		var moduleType = Module.getModuleType(module);
        		stages.get(moduleType.getIterationStage()).add(module);
        	}
        }

		for (var stage : stages) {
			for (var module : stage) {
        		var moduleType = Module.getModuleType(module);
				var moduleQuality = Module.getModuleQuality(module);
        		consumer.accept(moduleType, moduleQuality, module);
			}
		}
	}
}
