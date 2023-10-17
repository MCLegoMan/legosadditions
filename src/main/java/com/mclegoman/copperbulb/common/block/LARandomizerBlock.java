package com.mclegoman.copperbulb.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LARandomizerBlock extends Block {
	public static final BooleanProperty OUTPUT;
	public static final IntProperty VALUE;
	public LARandomizerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(OUTPUT, false).with(OUTPUT, false).with(VALUE, 1));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(OUTPUT, false).with(OUTPUT, false).with(VALUE, getRandomValue());
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return state.get(OUTPUT);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(OUTPUT) ? state.get(VALUE) : 0;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isReceivingRedstonePower(pos)) {
			if (!state.get(OUTPUT)) {
				world.setBlockState(pos, state.with(VALUE, getRandomValue()).with(OUTPUT, true));
			}
		} else {
			world.setBlockState(pos, state.with(OUTPUT, false));
		}
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.updateNeighborsAlways(pos, this);
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(OUTPUT);
		builder.add(VALUE);
	}

	private int getRandomValue() {
		Random random = Random.create();
		return random.nextBetween(1, 15);
	}

	static {
		OUTPUT = BooleanProperty.of("output");
		VALUE = IntProperty.of("value", 1, 15);
	}
}
