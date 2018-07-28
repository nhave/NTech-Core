package com.nhave.ntechcore.common.item;

import com.nhave.lib.library.client.mesh.CustomMeshDefinitionMetaItem;
import com.nhave.lib.library.client.render.IModelRegister;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMeta extends ItemBase implements IModelRegister
{
	private int rarity = 0;
	public String[] names;
	private int[] rarities;
	
	public ItemMeta(String name, int types)
	{
		super(name);
		this.names = new String[types];
		for (int i = 0; i < this.names.length; ++i)
		{
			this.names[i] = name;
		}
		this.rarity = 0;
		this.setHasSubtypes(true);
	}
	
	public ItemMeta(String name, String[] names, int rarity)
	{
		super(name);
		this.names = names;
		this.rarity = rarity;
		this.setHasSubtypes(true);
	}
	
	public ItemMeta(String name, String[] names, String type, int[] rarities)
	{
		this(name, names, 0);
		if (rarities.length == names.length) this.rarities = rarities;
	}
	
	public ItemMeta(String name, String[][] names)
	{
		super(name);
		this.rarities = new int[names.length];
		this.names = new String[names.length];
		for (int i = 0; i < names.length; ++i)
		{
			this.names[i] = names[i][0];
			this.rarities[i] = Integer.parseInt(names[i][1]);
		}
		this.rarity = 0;
		this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			for (int i = 0; i < names.length; ++i)
			{
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		int meta = Math.min(stack.getItemDamage(), names.length-1);
		return StringUtils.localize("item.ntechcore." + getItemName() + "." + names[meta] + ".name");
	}
	
	@Override
	public int getQuality(ItemStack stack)
	{
		if (this.rarities == null || this.rarities.length == 0) return this.rarity;
		int meta = Math.min(stack.getItemDamage(), rarities.length-1);	
		return rarities[meta];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for (int i = 0; i < this.names.length; ++i)
		{
			ModelLoader.registerItemVariants(this, new ResourceLocation(Reference.MODID + ":" + this.getItemName() + "_" + this.names[i].toLowerCase()));
		}
		
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelLoader.setCustomMeshDefinition(this, new CustomMeshDefinitionMetaItem(this.names));
	}
}