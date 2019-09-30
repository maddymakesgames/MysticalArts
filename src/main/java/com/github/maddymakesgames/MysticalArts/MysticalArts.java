package com.github.maddymakesgames.MysticalArts;

import com.github.maddymakesgames.MysticalArts.blocks.MysticalBlocks;

import com.github.maddymakesgames.MysticalArts.blocks.RitualTable;
import com.github.maddymakesgames.MysticalArts.items.MysticalItems;
import com.github.maddymakesgames.MysticalArts.recipes.MysticalRecipes;
import com.github.maddymakesgames.MysticalArts.recipes.RitualTableRecipe;
import com.github.maddymakesgames.MysticalArts.screens.RitualTableScreenController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.container.BlockContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MysticalArts implements ModInitializer {

	public static final String MODID = "mysticalarts";
	public static final Logger LOGGER = LogManager.getLogManager().getLogger(MODID);

	public static ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "item_group"), () -> new ItemStack(MysticalBlocks.RITUAL_TABLE.asItem()));
	public static Item.Settings ITEM_SETTINGS = new Item.Settings().group(ITEM_GROUP);

	@Override
	public void onInitialize() {

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Configurator.setLevel(MODID, Level.DEBUG);
		}

		MysticalRecipes.init();
		MysticalBlocks.init();
		MysticalItems.init();
		ContainerProviderRegistry.INSTANCE.registerFactory(RitualTable.ID, (syncId, id, player, buf) -> new RitualTableScreenController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
	}
}