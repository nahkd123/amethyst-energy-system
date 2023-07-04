package me.nahkd.amethystenergy.tools;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttributeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
}
