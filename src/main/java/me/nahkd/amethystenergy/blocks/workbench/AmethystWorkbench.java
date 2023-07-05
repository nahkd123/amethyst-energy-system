package me.nahkd.amethystenergy.blocks.workbench;

import me.nahkd.amethystenergy.blocks.AESBlocks;
import me.nahkd.amethystenergy.blocks.MultiblockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmethystWorkbench extends Block {
	// TODO: Get rid of storage and replace it with static screen thing
	private MultiblockTemplate multiblock;

	public AmethystWorkbench(Settings settings) {
		super(settings);
		multiblock = new MultiblockTemplate()
				.add(AESBlocks.CONDENSED_AMETHYST_BLOCK, new BlockPos(-1, 0, 1))
				.add(AESBlocks.CONDENSED_AMETHYST_BLOCK, new BlockPos(1, 0, 1))
				.add(AESBlocks.CONDENSED_AMETHYST_BLOCK, new BlockPos(1, 0, -1))
				.add(AESBlocks.CONDENSED_AMETHYST_BLOCK, new BlockPos(-1, 0, -1))
				.add(AESBlocks.AMETHYST_GLYPH_BLOCK, new BlockPos(0, 0, 1))
				.add(AESBlocks.AMETHYST_GLYPH_BLOCK, new BlockPos(0, 0, -1))
				.add(AESBlocks.AMETHYST_GLYPH_BLOCK, new BlockPos(1, 0, 0))
				.add(AESBlocks.AMETHYST_GLYPH_BLOCK, new BlockPos(-1, 0, 0));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			if (multiblock.check(world, pos)) {
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory(AmethystWorkbenchScreenHandler::new, getName()));
			}
		}

		return ActionResult.SUCCESS;
	}
}
