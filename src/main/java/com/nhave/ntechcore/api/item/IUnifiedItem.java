package com.nhave.ntechcore.api.item;

import net.minecraft.item.Item;

public interface IUnifiedItem
{
	public String getUnifiedName();
	
	public Item[] getFullSet();
}