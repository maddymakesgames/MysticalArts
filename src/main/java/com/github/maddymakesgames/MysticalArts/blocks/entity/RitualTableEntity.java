package com.github.maddymakesgames.MysticalArts.blocks.entity;


import com.github.maddymakesgames.MysticalArts.MysticalArts;
import com.github.maddymakesgames.MysticalArts.blocks.MysticalBlocks;

import com.github.maddymakesgames.MysticalArts.blocks.RitualTable;
import com.github.maddymakesgames.MysticalArts.recipes.MysticalRecipes;
import com.github.maddymakesgames.MysticalArts.recipes.RitualTableRecipe;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.logging.Level;

public class RitualTableEntity extends BlockEntity implements SidedInventory, BlockEntityClientSerializable, Tickable, PropertyDelegateHolder {
	protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(getInvSize(), ItemStack.EMPTY);
	private int ticks = 0;
	private int ticksTotal = 0;
	private int blood = 0;
	private int bloodLeft = 0;

	private PropertyDelegate propertyDelegate = new PropertyDelegate() {
		@Override
		public int get(int i) {
			switch(i) {
				case 1: return ticks;
				case 2: return ticksTotal;
				case 3: return blood;
				case 4: return getMaxBlood();
				default: return 0;
			}
		}

		@Override
		public void set(int i, int i1) {
			switch (i) {
				case 1: ticks = i1;
				case 3: blood += i1;
				default: return;
			}
		}

		@Override
		public int size() {
			return inventory.size();
		}
	};

	public RitualTableEntity() {
		super(MysticalBlocks.RITUAL_TABLE_ENTITY_TYPE);
	}

	@Override
	public void fromTag(CompoundTag compoundTag_1) {
		super.fromTag(compoundTag_1);
		ticks = compoundTag_1.getInt("ticks");
		ticksTotal = compoundTag_1.getInt("cookTime");
		blood = compoundTag_1.getInt("blood");
		bloodLeft = compoundTag_1.getInt("bloodLeft");
		Inventories.fromTag(compoundTag_1, inventory);
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag_1) {
		super.toTag(compoundTag_1);
		compoundTag_1.putInt("ticks", ticks);
		compoundTag_1.putInt("cookTime", ticksTotal);
		compoundTag_1.putInt("blood", blood);
		compoundTag_1.putInt("bloodLeft", bloodLeft);
		Inventories.toTag(compoundTag_1, inventory);
		return compoundTag_1;
	}

	@Override
	public int[] getInvAvailableSlots(Direction direction) {
		return new int[]{0,1};
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack itemStack) {
		return slot <= getInvSize();
	}

	@Override
	public boolean canInsertInvStack(int i, ItemStack itemStack, Direction direction) {
		return isValidInvStack(i, itemStack);
	}

	@Override
	public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
		return i == 1;
	}

	@Override
	public int getInvSize() {
		return 7;
	}

	@Override
	public boolean isInvEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getInvStack(int i) {
		return inventory.get(i);
	}

	@Override
	public ItemStack takeInvStack(int i, int i1) {
		return Inventories.splitStack(inventory, i, i1);
	}

	@Override
	public ItemStack removeInvStack(int i) {
		return Inventories.removeStack(inventory, i);
	}

	@Override
	public void setInvStack(int i, ItemStack itemStack) {
		inventory.set(i, itemStack);
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity playerEntity) {
		return true;
	}

	@Override
	public void clear() {
		inventory.clear();
	}


	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}

	@Override
	public void tick() {
		if (canRun()) {
			if (ticksTotal == 0) {
				ticksTotal = (int) (getRecipe().getProcessingTime())/4;
				bloodLeft = getRecipe().getCost();
			}
			boolean canTick = true;
			if(canTick) ticks++;
			setBlockActive(true);
			if (ticks >= ticksTotal) {
				craftItem();
				ticks = 0;
				ticksTotal = 0;
				bloodLeft = 0;
				if (inputsEmpty()) {
					setBlockActive(false);
				}
				updateEntity();
			}
		} else if (!canRun() && ticks > 0) {
			ticks = 0;
			ticksTotal = 0;
			bloodLeft = 0;
			setBlockActive(false);
		}
	}

	public boolean canRun() {
		ItemStack output = getOutputStack();
		if (inventory.get(0).isEmpty()
				|| output.isEmpty()
				|| inventory.get(5).getCount() > 64 || this.blood < getRecipe().getCost()) {
			return false;
		} else if (!inventory.get(5).isEmpty()) {
			return output.getItem() == inventory.get(5).getItem() || (inventory.get(5).getItem() == getRecipe().getBase().getItem() && inventory.get(5).getCount() == 1);
		}

		return true;
	}

	protected void serverTick() {

	}

	protected void updateEntity() {
		markDirty();
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}

	protected void setBlockActive(boolean active) {
		world.setBlockState(pos, world.getBlockState(pos).with(RitualTable.ACTIVE, active), 3);
	}

	protected void clientTick() {}

	private void craftItem() {
		ItemStack output = getOutputStack();
		if (!output.isEmpty()) {
			if (inventory.get(5).isEmpty()) {
				inventory.set(5, output);
			} else if(output.getItem() == inventory.get(5).getItem()){
				inventory.get(5).increment(1);
			}
			else {
				inventory.set(5, output);
			}
			inventory.get(0).decrement(1);
			inventory.get(1).decrement(1);
			inventory.get(2).decrement(1);
			inventory.get(3).decrement(1);
			inventory.get(4).decrement(1);
			blood -= bloodLeft/2;
			world.addParticle(ParticleTypes.EXPLOSION, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
		}
	}

	public ItemStack getOutputStack() {
		RitualTableRecipe recipe = getRecipe();
		return recipe != null ? recipe.getOutput().copy() : ItemStack.EMPTY;
	}

	public RitualTableRecipe getRecipe() {
		return world.getRecipeManager().getFirstMatch(MysticalRecipes.RITUAL, this, world).orElse(null);
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
	}

	public boolean isActive() {
		assert world != null;
		return world.getBlockState(pos).get(RitualTable.ACTIVE);
	}

	public boolean inputsEmpty() {
		return inventory.get(0) != ItemStack.EMPTY && inventory.get(1) != ItemStack.EMPTY && inventory.get(2) != ItemStack.EMPTY && inventory.get(3) != ItemStack.EMPTY && inventory.get(4) != ItemStack.EMPTY;
	}

	protected void sendPacket(ServerWorld w, CompoundTag tag) {
		tag.putInt("ticks", ticks);
		tag.putInt("cookTime", ticksTotal);
		sendPacket(w, new BlockEntityUpdateS2CPacket(getPos(), 127, tag));
	}

	protected void sendPacket(ServerWorld w, BlockEntityUpdateS2CPacket packet) {
		w.getPlayers(player -> player.squaredDistanceTo(new Vec3d(getPos())) < 24 * 24).forEach(player -> player.networkHandler.sendPacket(packet));
	}

	public int getTicks() {
		return ticks;
	}

	public int getTicksTotal() {
		return ticksTotal;
	}

	public int getMaxBlood() {
		return 1000;
	}

	private int min(int num1, int num2) {
		if(num1 < num2) return num1;
		else return num2;
	}
}