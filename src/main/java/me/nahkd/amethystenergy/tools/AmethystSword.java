package me.nahkd.amethystenergy.tools;

import java.util.Arrays;
import java.util.List;

import me.nahkd.amethystenergy.modules.Module;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import me.nahkd.amethystenergy.modules.contexts.ModuleAttackContext;
import me.nahkd.amethystenergy.modules.contexts.ModuleUseContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmethystSword extends AmethystTool {
	public AmethystSword(Settings settings) {
		super(settings, Arrays.asList(ModuleSlot.HANDLE, ModuleSlot.BINDING, ModuleSlot.SWORD_BLADE), 3, -2.4f);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

	@Override
    public boolean isSuitableFor(BlockState state) {
        return state.isOf(Blocks.COBWEB);
    }

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		var ctx = new ModuleAttackContext(stack, 1, target, attacker);
		forEachModules(stack, (type, quality, moduleData) -> type.onAttack(ctx, moduleData, quality));
		ctx.applyUpdates();

		if (ctx.durabilityUse > 0) {
			forEachModules(stack, (type, quality, moduleData) -> type.onItemDamage(ctx, moduleData, quality));
			ctx.applyUpdates();
			stack.damage(ctx.durabilityUse, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0f) {
			var ctx = new ModuleUseContext(stack, 1);
			forEachModules(stack, (type, quality, moduleData) -> type.onItemDamage(ctx, moduleData, quality)); // TODO: ModuleMineContext
			ctx.applyUpdates();
            if (ctx.durabilityUse > 0) stack.damage(ctx.durabilityUse, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }

        return true;
	}

	@Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 15.0f;
        }
        return state.isIn(BlockTags.SWORD_EFFICIENT) ? 1.5f : 1.0f;
    }
}
