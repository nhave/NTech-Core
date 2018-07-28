package com.nhave.ntechcore.common.item;

import com.nhave.lib.library.item.IOreRegister;
import com.nhave.ntechcore.core.NTechCore;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOredict extends ItemMeta implements IOreRegister
{
	public String ore;
	
	public ItemOredict(String name, String ore, String[][] names)
	{
		super(name, names);
		this.ore = ore;
	}
	
	@Override
	public void registerOres()
	{
		for (int i = 0; i < this.names.length; ++i)
		{
			NTechCore.logger.info("Registering Oredict entry \"" + this.ore + this.names[i] + "\" for Item : " + this.getRegistryName() + ":" + i);
			OreDictionary.registerOre(this.names[i], new ItemStack(this, 1, i));
		}
	}
}