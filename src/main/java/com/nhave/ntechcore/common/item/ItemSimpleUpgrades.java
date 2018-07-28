package com.nhave.ntechcore.common.item;

import java.util.List;

import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IItemUpgrade;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSimpleUpgrades extends ItemMeta implements IItemUpgrade
{
	public String[] nbts;
	
	public ItemSimpleUpgrades(String name, String[][] names)
	{
		super(name, names);
		this.nbts = new String[names.length];
		for (int i = 0; i < names.length; ++i)
		{
			this.nbts[i] = names[i][2];
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (StringUtils.isShiftKeyDown())
		{
			tooltip.add(StringUtils.format(StringUtils.localize("item.ntechcore." + getItemName() + ".name"), StringUtils.GREEN, StringUtils.ITALIC));
			int meta = Math.min(stack.getItemDamage(), names.length-1);
			
			String descUnlocalized = "tooltip.ntechcore." + getItemName() + "." + nbts[meta].toLowerCase();
			String descLocalized = StringUtils.localize(descUnlocalized);
			if (!descLocalized.equals(descUnlocalized))
			{
				TooltipHelper.addSplitString(tooltip, descLocalized, ";", StringUtils.GRAY);
			}
			else TooltipHelper.addSplitString(tooltip, StringUtils.localize("tooltip.ntechcore." + getItemName() + "." + names[meta]), ";", StringUtils.GRAY);
			
			String extraUnlocalized = "tooltip.ntechcore." + getItemName() + "." + names[meta] + ".extra";
			String extraLocalized = StringUtils.localize(extraUnlocalized);
			if (!extraLocalized.equals(extraUnlocalized))
			{
				TooltipHelper.addSplitString(tooltip, extraLocalized, ";", StringUtils.LIGHT_BLUE + StringUtils.ITALIC);
			}
			
		}
		else tooltip.add(StringUtils.shiftForInfo());
	}
	
	@Override
	public String getUpgradeNBT(ItemStack stack)
	{
		int meta = Math.min(stack.getItemDamage(), names.length-1);
		return this.nbts[meta];
	}
	
	@Override
	public boolean ignoreMeta(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public String getUpgradeName(ItemStack stack)
	{
		int meta = Math.min(stack.getItemDamage(), names.length-1);
		String unLocalized = "tooltip.ntechcore.mod.machine." + this.nbts[meta].toLowerCase();
		String localized = StringUtils.localize("tooltip.ntechcore.mod.machine." + this.nbts[meta].toLowerCase());
		return (unLocalized.equals(localized) ? this.getItemStackDisplayName(stack) : localized);
	}
}