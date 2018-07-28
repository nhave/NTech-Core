package com.nhave.ntechcore.client.eventhandler;

import java.util.ArrayList;
import java.util.List;

import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IUnifiedItem;
import com.nhave.ntechcore.common.itemupgrade.UpgradeManager;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TooltipEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void makeTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		if (stack == null) return;
		
		if (event.getToolTip().contains(StringUtils.RED + StringUtils.localize("tooltip.ntechcore.error.missingnbt"))) return;
		
		//Adding upgrade info to items.
		boolean isUpgrade = false;
		List<String> items = new ArrayList<String>();
		for (Item item : Item.REGISTRY)
		{
			if (UpgradeManager.hasUpgrades(item))
			{
				ItemStack upgradable = new ItemStack(item);
				if (UpgradeManager.canApplyUpgrade(upgradable, stack))
				{
					String name = item.getItemStackDisplayName(upgradable);
					
					if (item instanceof IUnifiedItem)
					{
						IUnifiedItem U = (IUnifiedItem) item;
						boolean unified = true;
						for (int i = 0; i < U.getFullSet().length; ++i)
						{
							Item host = U.getFullSet()[i];
							if (UpgradeManager.hasUpgrades(host))
							{
								if (!UpgradeManager.canApplyUpgrade(new ItemStack(host), stack)) unified = false;
							}
						}
						if (unified) name = StringUtils.localize(U.getUnifiedName());
					}
					
					if (!items.contains(name))
					{
						items.add(name);
						isUpgrade = true;
					}
				}
			}
		}
		if (isUpgrade)
		{
			if (StringUtils.isShiftKeyDown())
			{
				event.getToolTip().add("");
				event.getToolTip().add(StringUtils.localize("tooltip.ntechcore.mod.canuse") + ":");
				for (String name : items)
				{
					event.getToolTip().add("  " + StringUtils.format(name, StringUtils.YELLOW, StringUtils.ITALIC));
				}
			}
			else if (!StringUtils.isShiftKeyDown() && !event.getToolTip().contains(StringUtils.shiftForInfo()))
			{
				event.getToolTip().add(StringUtils.shiftForInfo());
			}
			
			//Moving the Registry name to the bottom of the tooltip.
			if (event.getFlags().isAdvanced())
			{
				event.getToolTip().remove(StringUtils.GRAY + stack.getItem().getRegistryName());
				event.getToolTip().add(StringUtils.GRAY + stack.getItem().getRegistryName());
			}
		}
	}
}