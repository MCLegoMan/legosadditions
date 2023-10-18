package com.mclegoman.legosadditions.common.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LARandomizerBlock extends HorizontalFacingBlock {
	public static final BooleanProperty OUTPUT;
	public static final IntProperty VALUE;
	public LARandomizerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(OUTPUT, false).with(OUTPUT, false).with(VALUE, 1).with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(VALUE, getRandomValue(1, ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()))).with(OUTPUT, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(VALUE);
	}
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(FACING) == direction && state.get(OUTPUT) ? state.get(VALUE) : 0;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.get(OUTPUT) && world.getEmittedRedstonePower(pos.offset(state.get(FACING)), state.get(FACING)) > 0) world.setBlockState(pos, state.with(OUTPUT, true).with(VALUE, getRandomValue(1, world.getEmittedRedstonePower(pos.offset(state.get(FACING)), state.get(FACING)))));
		else if (state.get(OUTPUT) && !(world.getEmittedRedstonePower(pos.offset(state.get(FACING)), state.get(FACING)) > 0)) world.setBlockState(pos, state.with(OUTPUT, false));
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.updateNeighborsAlways(pos, this);
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, VALUE, OUTPUT);
	}

	private int getRandomValue(int MIN, int MAX) {
		Random random = Random.create();
		return random.nextBetween(Math.max(Math.min(MIN, MAX), 0), Math.min(Math.max(MAX, MIN), 15));
	}

	static {
		OUTPUT = BooleanProperty.of("output");
		VALUE = IntProperty.of("value", 0, 15);
	}
}
