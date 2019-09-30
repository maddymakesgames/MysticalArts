package com.github.maddymakesgames.MysticalArts.screens;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import com.github.maddymakesgames.MysticalArts.MysticalArtsClient;
import com.github.maddymakesgames.MysticalArts.recipes.MysticalRecipes;
import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class RitualTableScreenController extends CottonScreenController {


	public RitualTableScreenController(int syncId, PlayerInventory playerInventory, BlockContext context) {
		super(MysticalRecipes.RITUAL, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

		WGridPanel rootPanel = (WGridPanel) getRootPanel();

		rootPanel.add(new WLabel(new TranslatableText("block.mysticalarts.ritual_table"), WLabel.DEFAULT_TEXT_COLOR), 3, 0);

		WBar ProgressBar = new WBar(new Identifier(MysticalArts.MODID, "textures/gui/circle.png"), new Identifier(MysticalArts.MODID, "textures/gui/circle_fill.png"), 1, 2, WBar.Direction.UP);
		rootPanel.add(ProgressBar, 1, 1, 7, 7);

		WBar ResourceBar = new WBar(new Identifier(MysticalArts.MODID, "textures/gui/bar.png"), new Identifier(MysticalArts.MODID, "textures/gui/blood_fill.png"), 3, 4, WBar.Direction.RIGHT);
		ResourceBar.withTooltip(String.format("%s.%s.%s", "info", MysticalArts.MODID, "blood_with_max"));
		rootPanel.add(ResourceBar, 2, 7 ,6, 1);

		rootPanel.add(WItemSlot.of(blockInventory, 0), 4, 1);
		rootPanel.add(WItemSlot.of(blockInventory, 1), 7, 3);
		rootPanel.add(WItemSlot.of(blockInventory, 2), 6, 6);
		rootPanel.add(WItemSlot.of(blockInventory, 3), 2, 6);
		rootPanel.add(WItemSlot.of(blockInventory, 4), 1, 3);
		rootPanel.add(WItemSlot.of(blockInventory, 6), 1, 7);

		rootPanel.add(WItemSlot.outputOf(blockInventory, 5), 4, 4);

		rootPanel.add(this.createPlayerInventoryPanel(), 0, 8);

		rootPanel.validate(this);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 5;
	}
}
