package me.nahkd.amethystenergy.blocks;

import java.util.List;

import me.nahkd.amethystenergy.modules.MatterCondenserVialModule;
import me.nahkd.amethystenergy.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MatterCondenser extends Block {
	public static final IntProperty CONDENSE = IntProperty.of("condense", 0, 3);

	public MatterCondenser(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(CONDENSE, 0));
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(CONDENSE);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		var condenseLevel = state.get(CONDENSE);

		for (var dir : Direction.values()) {
			var nearPos = pos.add(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
			var nearState = world.getBlockState(nearPos);
			if (nearState.getBlock() != Blocks.BUDDING_AMETHYST) continue;
			if (random.nextFloat() < 0.23f) continue;
			condenseLevel++;
		}

		world.setBlockState(pos, state.with(CONDENSE, Math.max(Math.min(condenseLevel, 3), 0)));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var condenseLevel = state.get(CONDENSE);
		if (condenseLevel == 0) return ActionResult.PASS;
		var received = (condenseLevel * condenseLevel) * 0.003f;

		var heldStack = player.getStackInHand(hand);
		if (heldStack.isEmpty() || !(heldStack.getItem() instanceof MatterCondenserVialModule)) return ActionResult.PASS;
		var heldModuleData = heldStack.getOrCreateSubNbt(Module.TAG_MODULE);
		var current = heldModuleData.getFloat(MatterCondenserVialModule.TAG_CONDENSED);
		if (current >= 1f) return ActionResult.PASS;

		current = Math.max(Math.min(current + received, 1f), 0f);
		heldModuleData.putFloat(MatterCondenserVialModule.TAG_CONDENSED, current);
		world.setBlockState(pos, state.with(CONDENSE, 0));
		return ActionResult.SUCCESS;
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		tooltip.add(Text.literal("Click with the vial to harvest condensed matters").styled(s -> s.withColor(Formatting.GRAY)));
	}
}
