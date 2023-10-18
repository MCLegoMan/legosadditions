package com.mclegoman.legosadditions.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class LATFlipFlopBlock extends Block {
	public static final BooleanProperty INPUT;
	public static final BooleanProperty OUTPUT;
	public LATFlipFlopBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(INPUT, false).with(OUTPUT, false));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(INPUT, false).with(OUTPUT, false);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return state.get(OUTPUT);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return 15;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isReceivingRedstonePower(pos)) {
			if (!state.get(INPUT)) {
				world.setBlockState(pos, state.with(INPUT, true).with(OUTPUT, !state.get(OUTPUT)), 2);
			}
		} else {
			world.setBlockState(pos, state.with(INPUT, false), 3);
		}
		world.scheduleBlockTick(pos, this, 1);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.updateNeighbors(pos, this);
		world.scheduleBlockTick(pos, this, 1);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(INPUT);
		builder.add(OUTPUT);
	}

	static {
		INPUT = BooleanProperty.of("input");
		OUTPUT = BooleanProperty.of("output");
	}
}
