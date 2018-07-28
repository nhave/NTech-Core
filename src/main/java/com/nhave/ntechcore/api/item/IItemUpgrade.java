package com.nhave.ntechcore.api.item;

import net.minecraft.item.ItemStack;

public interface IItemUpgrade
{
	public String getUpgradeNBT(ItemStack upgrade);
	
	public boolean ignoreMeta(ItemStack upgrade);
	
	public String getUpgradeName(ItemStack upgrade);
}