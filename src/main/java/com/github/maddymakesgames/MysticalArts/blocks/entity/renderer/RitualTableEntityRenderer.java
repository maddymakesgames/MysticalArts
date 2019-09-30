package com.github.maddymakesgames.MysticalArts.blocks.entity.renderer;

import com.github.maddymakesgames.MysticalArts.blocks.MysticalBlocks;
import com.github.maddymakesgames.MysticalArts.blocks.entity.RitualTableEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class RitualTableEntityRenderer extends BlockEntityRenderer<RitualTableEntity> {

	@Override
	public void render(RitualTableEntity rte, double x, double y, double z, float partialTicks, int destroyStage) {
		double radiusModifier = 0;
		double bounce = Math.sin((rte.getWorld().getTime() + partialTicks) / 8.0) / 16.0;
		double yModifier = bounce/2;

		PropertyDelegate pd = rte.getPropertyDelegate();

		if(rte.isActive()) {
			double total = pd.get(2) == 0 ? 1 : pd.get(2);
			radiusModifier = (pd.get(1)/total)/2;
			yModifier = (pd.get(1)/total)*0.4;
		}

		for(int i = 0; i < 5; i++) {
			// Render item
			GlStateManager.pushMatrix();
			double[] point = getCircleLocation((rte.getWorld().getTime() + partialTicks)/32, 5*i, 0.5-radiusModifier);
			GlStateManager.translated(x + point[0] + 0.5, y+0.8 + yModifier, z + point[1] + 0.5);
			if(rte.isActive()) GlStateManager.rotatef((rte.getWorld().getTime() + partialTicks) * 4, 0, 1, 0);
			MinecraftClient.getInstance().getItemRenderer().renderItem(rte.getInvStack(4-i), ModelTransformation.Type.GROUND);
			GlStateManager.popMatrix();

			// Render item still on base if there are multiple
			if(rte.isActive() && rte.getInvStack(4-i).getCount() > 1) {
				GlStateManager.pushMatrix();
				point = getCircleLocation((rte.getWorld().getTime() + partialTicks)/32, 5*i, 0.5);
				GlStateManager.translated(x + point[0] + 0.5, y+0.8 + bounce/2, z + point[1] + 0.5);
				MinecraftClient.getInstance().getItemRenderer().renderItem(rte.getInvStack(4-i), ModelTransformation.Type.GROUND);
				GlStateManager.popMatrix();
			}

			//Render evil beacon base
			if(!rte.getInvStack(4-i).isEmpty()) {
				GlStateManager.pushMatrix();
				point = getCircleLocation((rte.getWorld().getTime() + partialTicks) / 32, 5 * i, 0.5);
				GlStateManager.translated(x + point[0] + 0.5, y + 0.55 + bounce/2, z + point[1] + 0.5);
				MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(MysticalBlocks.EVIL_BEACON_BASE.asItem()), ModelTransformation.Type.GROUND);
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.pushMatrix();
		GlStateManager.translated(x + 0.5, y+1.2+bounce, z + 0.5);
		GlStateManager.rotatef((rte.getWorld().getTime() + partialTicks) * 4, 0, 1, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(rte.getInvStack(5), ModelTransformation.Type.GROUND);
		GlStateManager.popMatrix();
	}

	public double[] getCircleLocation(double degree, double initAngle, double radius) {
		double degrees = (initAngle + degree) % 360;
		double[] rotated = new double[2];

		rotated[0] = radius * Math.cos(degrees);
		rotated[1] = radius * Math.sin(degrees);

		return rotated;
	}
}
