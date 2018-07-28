package com.nhave.ntechcore.common.item;

import java.util.List;

import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IItemUpgrade;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJumpUpgrade extends ItemMeta implements IItemUpgrade
{
	public ItemJumpUpgrade(String name, String[][] names)
	{
		super(name, names);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (StringUtils.isShiftKeyDown())
		{
			int meta = Math.min(stack.getItemDamage(), names.length-1);	
			TooltipHelper.addSplitString(tooltip, StringUtils.localize("tooltip.ntechcore.item." + getItemName() + "." + names[meta]), ";", StringUtils.GRAY);
		}
		else tooltip.add(StringUtils.shiftForInfo());
	}
	
	public int getInAirJumps(ItemStack stack)
	{
		return (stack.getMetadata() == 1 ? 2 : 1);
	}
	
	public float getJumpHeight(ItemStack stack)
	{
		switch (stack.getMetadata())
		{
		case 1:
			return 0.5F;
		case 2:
			return 0.2F;
		default:
			return 0.75F;
		}
	}
	
	public float getJumpVelocity(ItemStack stack)
	{
		return (stack.getMetadata() == 2 ? 0.6F : 0);
	}
	
	@Override
	public String getUpgradeNBT(ItemStack upgrade)
	{
		return "JUMPKIT";
	}
	
	@Override
	public boolean ignoreMeta(ItemStack upgrade)
	{
		return true;
	}
	
	@Override
	public String getUpgradeName(ItemStack upgrade)
	{
		return StringUtils.localize("tooltip.ntechcore.mod.jumpkit");
	}
}