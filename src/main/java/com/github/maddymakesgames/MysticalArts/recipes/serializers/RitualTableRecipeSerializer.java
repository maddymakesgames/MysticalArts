package com.github.maddymakesgames.MysticalArts.recipes.serializers;

import com.github.maddymakesgames.MysticalArts.recipes.RitualTableRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

import java.util.Iterator;

public class RitualTableRecipeSerializer implements RecipeSerializer<RitualTableRecipe> {
	@Override
	public RitualTableRecipe read(Identifier id, JsonObject jsonObject) {
		DefaultedList<Ingredient> input = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
		ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "output"));
		int processTime = JsonHelper.getInt(jsonObject, "processTime", 1800);
		String resource;
		int cost;
		try {
			resource = JsonHelper.getString(jsonObject, "resource");
			cost = JsonHelper.getInt(jsonObject, "cost");
		}
		catch (Exception e) {
			resource = "";
			cost = 0;
		}
		ItemStack base;
		try {
			base = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "base"));
		}
		catch (Exception e) {
			base = ItemStack.EMPTY;
		}
		return new RitualTableRecipe(input, output, processTime, cost, resource, base);
	}

	@Override
	public RitualTableRecipe read(Identifier id, PacketByteBuf buf) {
		int size = buf.readVarInt();
		DefaultedList<Ingredient> inputs = DefaultedList.ofSize(size, Ingredient.EMPTY);

		for(int i = 0; i < inputs.size(); i++) {
			inputs.set(i, Ingredient.fromPacket(buf));
		}

		ItemStack output = buf.readItemStack();
		int processTime = buf.readInt();
		int cost = buf.readInt();
		String resource = buf.readString();
		ItemStack base = buf.readItemStack();
		return new RitualTableRecipe(inputs, output, processTime, cost, resource, base);
	}

	@Override
	public void write(PacketByteBuf buf, RitualTableRecipe recipe) {
		buf.writeVarInt(recipe.getInputs().size());
		Iterator inputs = recipe.getInputs().iterator();
		for(int i = 0; i < recipe.getInputs().size(); i++) {
			Ingredient ingredient = (Ingredient)inputs.next();
			ingredient.write(buf);
		}

		buf.writeItemStack(recipe.getOutput());
		buf.writeInt(recipe.getProcessingTime());
		buf.writeInt(recipe.getCost());
		buf.writeString(recipe.getResourceType());
		buf.writeItemStack(recipe.getBase());
	}

	private static DefaultedList<Ingredient> getIngredients(JsonArray jsonArray_1) {
		DefaultedList<Ingredient> defaultedList_1 = DefaultedList.of();

		for(int int_1 = 0; int_1 < jsonArray_1.size(); ++int_1) {
			Ingredient ingredient_1 = Ingredient.fromJson(jsonArray_1.get(int_1));
			if (!ingredient_1.isEmpty()) {
				defaultedList_1.add(ingredient_1);
			}
		}

		return defaultedList_1;
	}
}
