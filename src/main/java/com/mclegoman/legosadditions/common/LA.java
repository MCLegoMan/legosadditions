package com.mclegoman.legosadditions.common;

import com.mclegoman.legosadditions.common.block.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentStateManager;

public class LA implements ModInitializer {
	public static Block T_FLIP_FLOP_BLOCK = Registry.register(Registries.BLOCK, new Identifier("legosadditions", "t_flip_flop_block"), new LATFlipFlopBlock(FabricBlockSettings.create()));
	public static Item T_FLIP_FLOP_BLOCK_ITEM = Registry.register(Registries.ITEM, new Identifier("legosadditions", "t_flip_flop_block"), new BlockItem(T_FLIP_FLOP_BLOCK, new FabricItemSettings()));
	public static Block RANDOMIZER_BLOCK = Registry.register(Registries.BLOCK, new Identifier("legosadditions", "randomizer_block"), new LARandomizerBlock(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).strength(3.0F).requiresTool().solidBlock(Blocks::never)));
	public static Item RANDOMIZER_BLOCK_ITEM = Registry.register(Registries.ITEM, new Identifier("legosadditions", "randomizer_block"), new BlockItem(RANDOMIZER_BLOCK, new FabricItemSettings()));
	public static Block ADJUSTER_BLOCK = Registry.register(Registries.BLOCK, new Identifier("legosadditions", "adjuster"), new LAAdjusterBlock(FabricBlockSettings.create().breakInstantly().sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.DESTROY)));
	public static Item ADJUSTER_BLOCK_ITEM = Registry.register(Registries.ITEM, new Identifier("legosadditions", "adjuster"), new BlockItem(ADJUSTER_BLOCK, new FabricItemSettings()));
	public static Block ENDER_BARREL = Registry.register(Registries.BLOCK, new Identifier("legosadditions", "ender_barrel"), new LAEnderBarrel(FabricBlockSettings.create()));
	public static Item ENDER_BARREL_ITEM = Registry.register(Registries.ITEM, new Identifier("legosadditions", "ender_barrel"), new BlockItem(ENDER_BARREL, new FabricItemSettings()));
	public static LAEnderBarrelInventory ENDER_BARREL_INVENTORY;
	public static SoundEvent ENDER_BARREL_SOUND = Registry.register(Registries.SOUND_EVENT, new Identifier("legosadditions", "ender_barrel"), SoundEvent.of(new Identifier("legosadditions", "ender_barrel")));
	@Override
	public void onInitialize() {
		BlockRenderLayerMap.INSTANCE.putBlock(ADJUSTER_BLOCK, RenderLayer.getCutout());

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
			content.addAfter(Blocks.OBSERVER, T_FLIP_FLOP_BLOCK_ITEM);
			content.addAfter(T_FLIP_FLOP_BLOCK_ITEM, RANDOMIZER_BLOCK_ITEM);
			content.addAfter(Blocks.COMPARATOR, ADJUSTER_BLOCK_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
			content.addAfter(Blocks.ENDER_CHEST, ENDER_BARREL_ITEM);
		});
		ServerWorldEvents.LOAD.register((server, world) -> {
			PersistentStateManager persistentStateManager = server.getOverworld().getPersistentStateManager();
			ENDER_BARREL_INVENTORY = persistentStateManager.getOrCreate(LAEnderBarrelInventory.type, "legosadditions:ender_barrel");
		});
	}
}