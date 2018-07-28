package com.nhave.ntechcore.common.itemupgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IUpgradeHandler
{
	public void onUpgradeApplied(ItemStack upgradable, ItemStack upgrade);
	
	public void onUpgradeRemoved(ItemStack upgradable, ItemStack upgrade);
	
	public boolean onUpgradeTick(World world, EntityPlayer player, ItemStack stack);
}