package com.nhave.ntechcore.common.block;

import com.nhave.ntechcore.common.content.ModBlocks;
import com.nhave.ntechcore.common.content.ModCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class BlockBase extends Block
{
	private int quality = 0;
	
	public BlockBase(String name, Material materialIn)
	{
		super(materialIn);
		this.setRegistryName(name);
		this.setCreativeTab(ModCreativeTabs.getBlocks());
		this.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + name);
		
		ModBlocks.addBlock(this);
	}
	
	public BlockBase setQuality(int quality)
	{
		this.quality = quality;
		return this;
	}
	
	public int getQuality(ItemStack stack)
	{
		return this.quality;
	}
}