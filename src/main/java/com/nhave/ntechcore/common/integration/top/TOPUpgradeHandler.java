package com.nhave.ntechcore.common.integration.top;

import com.nhave.ntechcore.common.itemupgrade.IUpgradeHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TOPUpgradeHandler implements IUpgradeHandler
{
	@Override
	public void onUpgradeApplied(ItemStack upgradable, ItemStack upgrade)
	{
		NBTTagCompound tag = upgradable.getTagCompound();
		if (tag == null) tag = new NBTTagCompound();
        tag.setInteger("theoneprobe", 1);
        upgradable.setTagCompound(tag);
	}
	
	@Override
	public void onUpgradeRemoved(ItemStack upgradable, ItemStack upgrade)
	{
		NBTTagCompound tag = upgradable.getTagCompound();
		if (tag != null && tag.hasKey("theoneprobe")) tag.removeTag("theoneprobe");
	}
	
	@Override
	public boolean onUpgradeTick(World world, EntityPlayer player, ItemStack stack)
	{
		return false;
	}
}