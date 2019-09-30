package com.github.maddymakesgames.MysticalArts.blocks;

import com.github.maddymakesgames.MysticalArts.MysticalArts;
import com.github.maddymakesgames.MysticalArts.blocks.entity.RitualTableEntity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.jimfs.AttributeProvider;
import com.google.common.jimfs.File;
import com.google.common.jimfs.FileLookup;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.nio.file.attribute.FileAttributeView;

import static net.fabricmc.fabric.api.container.ContainerProviderRegistry.INSTANCE;

public class RitualTable extends Block implements BlockEntityProvider {

	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	public static final Identifier ID = new Identifier(MysticalArts.MODID, "ritual_table");

	public RitualTable(Settings blockSettings) {
		super(blockSettings);
		setDefaultState(getStateFactory().getDefaultState().with(ACTIVE, false));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new RitualTableEntity();
	}

	@Override
	public boolean activate(BlockState blockState_1, World world, BlockPos pos, PlayerEntity player, Hand hand_1, BlockHitResult blockHitResult_1) {
		if (!world.isClient) {
			ContainerProviderRegistry.INSTANCE.openContainer(ID, player, buf -> buf.writeBlockPos(pos));
		}
		return true;
	}

	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState stateFrom, PlayerEntity playerEntity_1) {
		BlockEntity be = world.getBlockEntity(pos);

		if(be instanceof SidedInventory) {
			ItemScatterer.spawn(world, pos, (SidedInventory) be);
			world.updateHorizontalAdjacent(pos, this);
		}
		ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(),new ItemStack(this.asItem()));
		super.onBreak(world, pos, stateFrom, playerEntity_1);
	}
}