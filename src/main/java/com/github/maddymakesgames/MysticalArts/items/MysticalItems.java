package com.github.maddymakesgames.MysticalArts.items;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MysticalItems {

	public static final Item RitualDagger = register("ritual_dagger", new Dagger(5, 1.0f, 1, 0.0f, MysticalArts.ITEM_SETTINGS));
	public static final Item CrystalDagger = register("crystal_dagger", new Dagger(9, 1.0f, 25, 1.0f, MysticalArts.ITEM_SETTINGS));
	public static final Item Soul = register("soul", new GlowingItem());
	public static final Item SoulDiamond = register("soul_diamond", new GlowingItem());

	public static void init() {

	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(MysticalArts.MODID, name), item);
		return item;
	}
}
