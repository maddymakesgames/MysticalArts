package com.github.maddymakesgames.MysticalArts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class Infuser extends Block implements BlockEntityProvider {
	public Infuser(Settings block$Settings_1) {
		super(block$Settings_1);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return null;
	}
}
