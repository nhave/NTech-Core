package com.nhave.ntechcore.client.render.tileentity;

import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.common.tileentity.TileEntityUpgradeStation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RenderTileUpgradeStation extends TileEntitySpecialRenderer
{
	public static Minecraft mc = Minecraft.getMinecraft();

	private static final ItemStack modelStackSlider = new ItemStack(ModItems.itemModels, 1, 0);
	private static final ItemStack modelStackMover = new ItemStack(ModItems.itemModels, 1, 1);
	private static final ItemStack modelStackLaser = new ItemStack(ModItems.itemModels, 1, 2);
	
	@Override
	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		renderTileEntityAt(te, x, y, z);
	}
	
	private void renderTileEntityAt(TileEntity tile, double x, double y, double z)
	{
		if (tile instanceof TileEntityUpgradeStation)
		{
			TileEntityUpgradeStation tileEntity = (TileEntityUpgradeStation) tile;
			
			GlStateManager.pushMatrix();
	        GlStateManager.translate(x, y, z);
			this.renderItem(tileEntity.getItemStacks()[0], tileEntity.getBlockMetadata());
	        GlStateManager.popMatrix();
	        
			GlStateManager.pushMatrix();
	        GlStateManager.translate(x, y, z);
	        renderSlider(tileEntity.getBlockMetadata(), tileEntity, tileEntity.sliderX, tileEntity.sliderZ);
	        GlStateManager.popMatrix();
		}
	}
	
	private void renderItem(ItemStack stack, int rotation)
	{
        RenderItem itemRenderer = mc.getRenderItem();
        if (!stack.isEmpty())
        {
			GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            if (stack.getItem() instanceof ItemBlock)
			{
				GlStateManager.translate(0.5F, 0.62F, 0.5F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);

				if (rotation == 5) GlStateManager.rotate(270, 0, 1, 0);
				else if (rotation == 4) GlStateManager.rotate(90, 0, 1, 0);
				else if (rotation == 3) GlStateManager.rotate(180, 0, 1, 0);
			}
			else
			{
				GlStateManager.translate(0.5F, 0.52F, 0.5F);
				GlStateManager.scale(0.7F, 0.7F, 0.7F);

				if (rotation == 4)
				{
					GlStateManager.rotate(90, 0, 1, 0);
				}
				else if (rotation == 5)
				{
					GlStateManager.rotate(270, 0, 1, 0);
				}
				else if (rotation == 3)
				{
					GlStateManager.rotate(180, 0, 1, 0);
				}
				
				GlStateManager.rotate(90, 1, 0, 0);
			}
            
			GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            
            GlStateManager.enableLighting();
			GlStateManager.popMatrix();
        }
	}
	
	private void renderSlider(int rotation, TileEntityUpgradeStation tile, float x, float z)
	{
        RenderItem itemRenderer = mc.getRenderItem();

		GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
		
		if (rotation == 4)
		{
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.translate(-1F, 0F, 0F);
		}
		else if (rotation == 5)
		{
			GlStateManager.rotate(270, 0, 1, 0);
			GlStateManager.translate(0F, 0F, -1F);
		}
		else if (rotation == 3)
		{
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.translate(-1F, 0F, -1F);
		}

		//GlStateManager.translate(0F + z, 0.552F, 0.5F);
		//GlStateManager.translate(0F + z, 0.552F, 0.5F);
		GlStateManager.translate(0.5F, 0.552F, 1F - x);
        
		GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();

		GlStateManager.pushMatrix();
		GlStateManager.scale(1.74F, 1.74F, 1.74F);
        itemRenderer.renderItem(modelStackSlider, ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();
        
		//GlStateManager.translate(0F, 0F, 0.5F - x);
		GlStateManager.translate(-0.5F + z, 0F, 0F);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.74F, 1.74F, 1.74F);
        itemRenderer.renderItem((tile.laserOn ? modelStackLaser : modelStackMover), ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();
        
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        
        GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}