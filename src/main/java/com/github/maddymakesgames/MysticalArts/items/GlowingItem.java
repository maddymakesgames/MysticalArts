package com.github.maddymakesgames.MysticalArts.items;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GlowingItem extends Item {
	public GlowingItem() {
		super(MysticalArts.ITEM_SETTINGS.maxDamage(0).maxCount(64));
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack itemStack_1) {
		return itemStack_1.getItem() == this;
	}
}
