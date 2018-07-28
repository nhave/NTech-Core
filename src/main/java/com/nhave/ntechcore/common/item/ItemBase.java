package com.nhave.ntechcore.common.item;

import com.nhave.ntechcore.common.content.ModCreativeTabs;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.core.Reference;
import com.nhave.rpgtooltips.common.item.EnumItemQuality;
import com.nhave.rpgtooltips.common.item.IItemQuality;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
    @Optional.Interface(iface = "com.nhave.rpgtooltips.common.item.IItemQuality", modid = "rpgtooltips")
})
public class ItemBase extends Item implements IItemQuality
{
	private int quality = 0;
	
	public ItemBase(String name)
	{
		this.setRegistryName(name);
		this.setCreativeTab(ModCreativeTabs.getItems());
		this.setUnlocalizedName(Reference.MODID + "." + name);
		
		ModItems.addItem(this);
	}
	
	@Override
    public Item setCreativeTab(CreativeTabs tab)
    {
		//ModItems.createTab(tab);
		return super.setCreativeTab(tab);
    }
	
	public String getItemName()
	{
		return this.getRegistryName().getResourcePath();
	}
	
	public ItemBase setQuality(int quality)
	{
		this.quality = quality;
		return this;
	}
	
	public int getQuality(ItemStack stack)
	{
		return this.quality;
	}
	
	@Optional.Method(modid = "rpgtooltips")
	@Override
	public EnumItemQuality getItemQuality(ItemStack stack)
	{
		switch (getQuality(stack))
		{
		case 0:
			return EnumItemQuality.COMMON;
		case 1:
			return EnumItemQuality.UNCOMMON;
		case 2:
			return EnumItemQuality.RARE;
		case 3:
			return EnumItemQuality.EPIC;
		case 4:
			return EnumItemQuality.LEGENDARY;
		case 5:
			return EnumItemQuality.RELIC;
		case 6:
			return EnumItemQuality.CREATIVE;
		default:
			return EnumItemQuality.COMMON;
		}
	}
}