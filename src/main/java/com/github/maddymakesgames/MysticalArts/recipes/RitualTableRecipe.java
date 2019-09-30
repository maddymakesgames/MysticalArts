package com.github.maddymakesgames.MysticalArts.recipes;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import com.github.maddymakesgames.MysticalArts.recipes.serializers.RitualTableRecipeSerializer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.MapUtil;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class RitualTableRecipe implements Recipe<Inventory> {

	protected final DefaultedList<Ingredient> inputs;
	protected final ItemStack output;
	protected final int processTime;
	protected final int cost;
	protected final String resource;
	protected final ItemStack base;

	public RitualTableRecipe(DefaultedList<Ingredient> inputs, ItemStack output, int processTime, int cost, String resource, ItemStack base) {
		this.inputs = inputs;
		this.output = output;
		this.processTime = processTime;
		this.cost = cost;
		this.resource = resource;
		this.base = base;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return !((this.base != ItemStack.EMPTY && this.base.getItem() != inventory.getInvStack(5).getItem()) || !testInv(inventory));
	}

	@Override
	public ItemStack craft(Inventory craftingInventory) {
		return output.copy();
	}

	@Override
	public boolean fits(int w, int h) {
		return w >= 1 && h >= 1;
	}

	@Override
	public ItemStack getOutput() {

		return this.output;
	}

	@Override
	public Identifier getId() {
		return new Identifier(MysticalArts.MODID, output.getItem().getTranslationKey());
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MysticalRecipes.RITUAL_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return MysticalRecipes.RITUAL;
	}

	public int getProcessingTime() {
		return this.processTime;
	}

	public DefaultedList<Ingredient> getInputs() {
		return this.inputs;
	}

	public int getCost() {
		return this.cost;
	}

	public String getResourceType() {
		return this.resource;
	}

	public ItemStack getBase() {
		return this.base;
	}

	private boolean testInv(Inventory inventory) {
		HashMap<Item, Integer> InputMap = new HashMap<>();
		for(int i = 0; i < this.inputs.size(); i++) {
			Item inputItem = this.inputs.get(i).getStackArray()[0].getItem();
			if(!InputMap.containsKey(inputItem))InputMap.put(inputItem, 1);
			else InputMap.put(inputItem, InputMap.get(inputItem)+1);
		}
		HashMap<Item, Integer> InvMap = new HashMap<>();
		for(int i = 0; i < 5; i++) {
			Item inputItem = inventory.getInvStack(i).getItem();
			if(!InvMap.containsKey(inputItem))InvMap.put(inputItem, 1);
			else InvMap.put(inputItem, InvMap.get(inputItem)+1);
		}
		return InputMap.entrySet().equals(InvMap.entrySet());
	}
}
