package com.nhave.ntechcore.common.itemupgrade;

import java.util.List;

import javax.annotation.Nonnull;

import com.nhave.lib.library.util.ItemUtil;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IArmorPlate;
import com.nhave.ntechcore.common.content.ModItems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpgradeHelper
{
	public static boolean hasArmorPlate(@Nonnull ItemStack stack)
	{
		ItemStack armorplate = ItemUtil.getItemFromStack(stack, "ARMORPLATE");
		if (armorplate != null && armorplate.getItem() instanceof IArmorPlate)
		{
			return true;
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public static void addUpgradeInfo(@Nonnull ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		Item item = stack.getItem();
		if (UpgradeManager.hasUpgrades(item))
		{
			if (StringUtils.isControlKeyDown())
			{
				tooltip.add("");
				tooltip.add(StringUtils.localize("tooltip.ntechcore.mod.all") + ":");
				
				NonNullList<String> nbtList = NonNullList.create();
				NonNullList<String> namesList = NonNullList.create();
				NonNullList<ItemStack> upgrades = UpgradeManager.getUpgrades(item);
				for (ItemStack upgrade : upgrades)
				{
					ItemStack installed = ItemUtil.getItemFromStack(stack, UpgradeManager.getUpgradeNBT(upgrade));
					String nbt = UpgradeManager.getUpgradeNBT(upgrade);
					String name = UpgradeManager.getUpgradeName(upgrade);
					
					if (!namesList.contains(name))
					{
						if (!nbtList.contains(nbt))
						{
							if (installed != null && !installed.isEmpty())
							{
								String itemName = installed.getItem().getItemStackDisplayName(installed);
								tooltip.add("  " + StringUtils.format(itemName, StringUtils.GREEN, StringUtils.ITALIC));
								
								String nbt1 = UpgradeManager.getUpgradeNBT(installed);
								if (nbt1 != null) nbtList.add(nbt1);
							}
							else tooltip.add("  " + StringUtils.format(name, StringUtils.RED, StringUtils.ITALIC));
						}
						namesList.add(name);
					}
				}
			}
			if (!StringUtils.isControlKeyDown())
			{
				if (StringUtils.isShiftKeyDown()) tooltip.add("");
				tooltip.add("§7" + StringUtils.localize("tooltip.ntechcore.details.ctrl1") + " §e§o" + StringUtils.localize("tooltip.ntechcore.details.ctrl2") + " §r§7" + StringUtils.localize("tooltip.ntechcore.details.ctrl3") + "§r");
			}
		}
	}
	
	public static int getMachineSpeed(ItemStack stack)
	{
		ItemStack upgrade = ItemUtil.getItemFromStack(stack, "SPEED");
		if (upgrade != null && upgrade.getItem() == ModItems.itemMachineUpgrades)
		{
			int meta = upgrade.getItemDamage();
			switch (meta)
			{
			case 0:
				return 1;
			case 1:
				return 2;
			}
		}
		return 0;
	}
	
	public static int getMachineEfficiency(ItemStack stack)
	{
		ItemStack upgrade = ItemUtil.getItemFromStack(stack, "EFFICIENCY");
		if (upgrade != null && upgrade.getItem() == ModItems.itemMachineUpgrades)
		{
			int meta = upgrade.getItemDamage();
			switch (meta)
			{
			case 2:
				return 1;
			case 3:
				return 2;
			}
		}
		return 0;
	}
	
	public static void setMachineUpgrade(ItemStack stack, String nbt, int level)
	{
		if (level <= 0) return;
		int meta = 0;
		switch (nbt)
		{
		case "SPEED":
			meta = (level == 2 ? 1 : 0);
			break;
		case "EFFICIENCY":
			meta = (level == 2 ? 3 : 2);
			break;
		}
		ItemUtil.addItemToStack(stack, new ItemStack(ModItems.itemMachineUpgrades, 1, meta), nbt);
	}
}