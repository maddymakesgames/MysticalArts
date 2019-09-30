package com.github.maddymakesgames.MysticalArts.recipes;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import com.github.maddymakesgames.MysticalArts.recipes.serializers.RitualTableRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MysticalRecipes {
	public static RecipeType<RitualTableRecipe> RITUAL = buildRecipeType(new Identifier(MysticalArts.MODID, "ritual"));
	public static RecipeSerializer<RitualTableRecipe> RITUAL_SERIALIZER = new RitualTableRecipeSerializer();

	public static void init() {
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MysticalArts.MODID, "ritual"), RITUAL);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MysticalArts.MODID, "ritual"), RITUAL_SERIALIZER);
	}

	private static <T extends Recipe<?>> RecipeType<T> buildRecipeType(Identifier id) {
		return new RecipeType<T>() {
			public String toString() {
				return id.getPath();
			}
		};
	}

}
