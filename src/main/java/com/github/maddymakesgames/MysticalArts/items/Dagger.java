package com.github.maddymakesgames.MysticalArts.items;

import com.github.maddymakesgames.MysticalArts.blocks.MysticalBlocks;
import com.github.maddymakesgames.MysticalArts.blocks.entity.RitualTableEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Dagger extends SwordItem {
	public int soulDropChance;
	public float healAmount;

	public Dagger(int damage, float attackSpeed, int soulDropChance, float healAmount, Settings settings) {
		super(ToolMaterials.IRON, damage, attackSpeed, settings);
		this.soulDropChance = soulDropChance;
		this.healAmount = healAmount;
	}

	@Override
	public boolean postHit(ItemStack itemStack_1, LivingEntity livingEntity_1, LivingEntity livingEntity_2) {
		if(livingEntity_1.getHealth() == 0.0f) {
			livingEntity_2.heal(healAmount);
			double rng = Math.random()*100;
			System.out.println(rng);
			if(rng < soulDropChance) livingEntity_1.dropItem(MysticalItems.Soul);
		}
		return super.postHit(itemStack_1, livingEntity_1, livingEntity_2);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
		BlockPos lowerBlock = playerEntity_1.getBlockPos().down();
		if(world_1.getBlockState(lowerBlock).getBlock() == MysticalBlocks.RITUAL_TABLE) {
			((RitualTableEntity)world_1.getBlockEntity(lowerBlock)).getPropertyDelegate().set(3, 100);
			playerEntity_1.damage(DamageSource.MAGIC, 3.0f);
		}

		return super.use(world_1, playerEntity_1, hand_1);
	}
}
