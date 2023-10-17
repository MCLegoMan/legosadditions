package com.mclegoman.copperbulb.common;

import com.mclegoman.copperbulb.common.block.LARandomizerBlock;
import com.mclegoman.copperbulb.common.block.LATFlipFlopBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LA implements ModInitializer {
	public static Block T_FLIP_FLOP_BLOCK = Registry.register(Registries.BLOCK, new Identifier("legosadditions", "t_flip_flop_block"), new LATFlipFlopBlock(FabricBlockSettings.create()));
	public static Item T_FLIP_FLOP_BLOCK_ITEM = Registry.register(Registries.ITEM, new Identifier("legosadditions", "t_flip_flop_block"), new BlockItem(T_FLIP_FLOP_BLOCK, new FabricItemSettings()));
	public static Block RANDOMIZER_BLOCK = Registry.register(Registries.BLOCK, new Identifier("legosadditions", "randomizer_block"), new LARandomizerBlock(FabricBlockSettings.create()));
	public static Item RANDOMIZER_BLOCK_ITEM = Registry.register(Registries.ITEM, new Identifier("legosadditions", "randomizer_block"), new BlockItem(RANDOMIZER_BLOCK, new FabricItemSettings()));
	@Override
	public void onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
			content.addAfter(Blocks.OBSERVER, T_FLIP_FLOP_BLOCK_ITEM);
			content.addAfter(T_FLIP_FLOP_BLOCK_ITEM, RANDOMIZER_BLOCK_ITEM);
		});
	}
}