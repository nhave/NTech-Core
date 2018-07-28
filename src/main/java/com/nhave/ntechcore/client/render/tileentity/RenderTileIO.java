package com.nhave.ntechcore.client.render.tileentity;

import com.nhave.ntechcore.common.tileentity.TileEntityIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RenderTileIO extends TileEntitySpecialRenderer
{
	public static Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		renderTileEntityAt(te, x, y, z);
	}
	
	private void renderTileEntityAt(TileEntity tile, double x, double y, double z)
	{
		if (tile instanceof TileEntityIO)
		{
			TileEntityIO tileEntity = (TileEntityIO) tile;
			ItemStack stack = tileEntity.getItemStacks()[0];
			
			GlStateManager.pushMatrix();
	        GlStateManager.translate(x + 0.3F, y, z + 0.6F);
	        
	        ItemStack[] items = tileEntity.getItemStacks();
			for (int i = 0; i < items.length; ++i)
			{
		        GlStateManager.translate(0, 0, -0.3F);
		        if (i == 3 || i == 6) GlStateManager.translate(-0.3F, 0, 0.9F);
				this.renderItem(tileEntity.getItemStacks()[i]);
			}
	        
	        GlStateManager.popMatrix();
		}
	}
	
	private void renderItem(ItemStack stack)
	{
        RenderItem itemRenderer = mc.getRenderItem();
        if (!stack.isEmpty())
        {
			GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

			GlStateManager.translate(0.5F, 0.625F, 0.5F);
			if (stack.getItem() instanceof ItemBlock)
			{
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}
			else
			{
				GlStateManager.scale(0.25F, 0.25F, 0.25F);
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
}