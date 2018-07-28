package com.nhave.ntechcore.common.content;

import com.nhave.ntechcore.core.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs
{
	private static CreativeTab TAB_ITEMS;
	private static CreativeTab TAB_BLOCKS;
	private static CreativeTab TAB_TOOLS;
	private static CreativeTab TAB_ARMOR;
	private static CreativeTab TAB_UPGRADES;
	
	public static CreativeTab getItems()
	{
		if (TAB_ITEMS == null)
		{
			TAB_ITEMS = new CreativeTab(Reference.MODID + ".items");
		}
		return TAB_ITEMS;
	}
	
	public static CreativeTab getBlocks()
	{
		if (TAB_BLOCKS == null)
		{
			TAB_BLOCKS = new CreativeTab(Reference.MODID + ".blocks");
		}
		return TAB_BLOCKS;
	}
	
	public static CreativeTab getTools()
	{
		if (TAB_TOOLS == null)
		{
			TAB_TOOLS = new CreativeTab(Reference.MODID + ".tools");
		}
		return TAB_TOOLS;
	}
	
	public static CreativeTab getArmor()
	{
		if (TAB_ARMOR == null)
		{
			TAB_ARMOR = new CreativeTab(Reference.MODID + ".armor");
		}
		return TAB_ARMOR;
	}
	
	public static CreativeTab getUpgrades()
	{
		if (TAB_UPGRADES == null)
		{
			TAB_UPGRADES = new CreativeTab(Reference.MODID + ".upgrades");
		}
		return TAB_UPGRADES;
	}
	
	static class CreativeTab extends CreativeTabs
	{
		private ItemStack icon = ItemStack.EMPTY;
		
		public CreativeTab(String label)
		{
			super(label);
		}
		
		public void setTabIconItem(ItemStack stack)
		{
			this.icon = stack;
		}

		@Override
		public ItemStack getTabIconItem()
		{
			return this.icon;
		}
	}
}