package me.nahkd.amethystenergy.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// Could have used StructureTemplate
public class MultiblockTemplate {
	private static final Direction[] CHECK_DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

	private List<Block> templateBlocks = new ArrayList<>();
	private List<BlockPos> templateOffsets = new ArrayList<>();
	private boolean allowRotate = true;

	public MultiblockTemplate setAllowRotate(boolean allowRotate) {
		this.allowRotate = allowRotate;
		return this;
	}

	public MultiblockTemplate add(Block block, BlockPos offset) {
		templateBlocks.add(block);
		templateOffsets.add(offset);
		return this;
	}

	public boolean check(World world, BlockPos origin, Direction rotation) {
		var rotCC = rotation.rotateYCounterclockwise();
		var tx = new BlockPos(rotation.getOffsetX(), rotation.getOffsetY(), rotation.getOffsetZ());
		var ty = new BlockPos(0, 1, 0);
		var tz = new BlockPos(rotCC.getOffsetX(), rotCC.getOffsetY(), rotCC.getOffsetZ());

		for (int i = 0; i < templateBlocks.size(); i++) {
			var templateBlock = templateBlocks.get(i);
			var templateOffset = templateOffsets.get(i);
			var worldOffset = origin.add(tx.multiply(templateOffset.getX())).add(ty.multiply(templateOffset.getY())).add(tz.multiply(templateOffset.getZ()));
			var worldBlock = world.getBlockState(worldOffset).getBlock();
			if (worldBlock != templateBlock) return false;
		}

		return true;
	}

	public boolean check(World world, BlockPos origin) {
		if (!allowRotate) return check(world, origin, Direction.NORTH);
		for (var dir : CHECK_DIRECTIONS) if (check(world, origin, dir)) return true;
		return false;
	}
}
