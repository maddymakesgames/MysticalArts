package com.github.maddymakesgames.MysticalArts.blocks;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import com.github.maddymakesgames.MysticalArts.blocks.entity.*;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MysticalBlocks {

	public static final Block RITUAL_TABLE = register("ritual_table", new RitualTable(FabricBlockSettings.copy(Blocks.IRON_BLOCK).build()));
	public static final Block EVIL_BEACON_BASE = register("evil_beacon_core", new Block(FabricBlockSettings.copy(Blocks.BEACON).build()));

	public static final BlockEntityType<RitualTableEntity> RITUAL_TABLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(MysticalArts.MODID, "ritual_table_entity"), BlockEntityType.Builder.create(RitualTableEntity::new, RITUAL_TABLE).build(null));

	public static void init() {

	}

	public static Block register(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(MysticalArts.MODID, name), block);
		BlockItem item = new BlockItem(block, MysticalArts.ITEM_SETTINGS);
		Registry.register(Registry.ITEM, new Identifier(MysticalArts.MODID, name), item);
		return block;
	}
}