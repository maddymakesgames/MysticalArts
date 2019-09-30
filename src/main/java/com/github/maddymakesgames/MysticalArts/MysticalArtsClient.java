package com.github.maddymakesgames.MysticalArts;

import com.github.maddymakesgames.MysticalArts.blocks.MysticalBlocks;
import com.github.maddymakesgames.MysticalArts.blocks.RitualTable;
import com.github.maddymakesgames.MysticalArts.blocks.entity.RitualTableEntity;
import com.github.maddymakesgames.MysticalArts.blocks.entity.renderer.RitualTableEntityRenderer;
import com.github.maddymakesgames.MysticalArts.screens.RitualTableScreen;
import com.github.maddymakesgames.MysticalArts.screens.RitualTableScreenController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;

public class MysticalArtsClient implements ClientModInitializer {

	public static Identifier bar = new Identifier(MysticalArts.MODID, "textures/gui/progress_bar.png");
	public static Identifier barBG = new Identifier(MysticalArts.MODID, "textures/gui/progress_bar_bg.png");


	@Override
	public void onInitializeClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(RitualTable.ID,
				(syncId, identifier, player, buf) -> new RitualTableScreen(
						new RitualTableScreenController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
		BlockEntityRendererRegistry.INSTANCE.register(RitualTableEntity.class, new RitualTableEntityRenderer());
	}
}
