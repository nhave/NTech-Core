package com.nhave.ntechcore.common.item;

import com.nhave.lib.library.item.INTechWrench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ItemWrench extends ItemBase implements INTechWrench
{
	public ItemWrench(String name)
	{
		super(name);
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean canUseWrench(ItemStack item, EntityPlayer player, EnumHand hand, BlockPos pos)
	{
		return true;
	}

	@Override
	public void onWrenchUsed(ItemStack item, EntityPlayer player, EnumHand hand, BlockPos pos)
	{
		player.swingArm(hand);
	}
}