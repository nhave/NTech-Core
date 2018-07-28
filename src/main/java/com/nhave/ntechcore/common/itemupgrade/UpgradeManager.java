package com.nhave.ntechcore.common.itemupgrade;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.ntechcore.api.item.IArmorPlate;
import com.nhave.ntechcore.api.item.IItemUpgrade;
import com.nhave.ntechcore.api.item.armor.IPlatedArmor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class UpgradeManager
{
	private static final Map<Item, String> UPGRADE_NBT = Maps.newHashMap();
	private static final Map<Item, IUpgradeHandler> HANDLERS = Maps.newHashMap();
	private static final Map<Item, NonNullList<ItemStack>> UPGRADES = Maps.newHashMap();
	
	public static boolean hasUpgrades(@Nonnull Item item)
	{
		if (UPGRADES.containsKey(item))
		{
			List<ItemStack> list = UPGRADES.get(item);
			return (list != null && list.size() > 0);
		}
		return false;
	}
	
	public static void addUpgrade(@Nonnull Item item, @Nonnull ItemStack stack)
	{
		NonNullList<ItemStack> list = (hasUpgrades(item) ? UPGRADES.get(item) : NonNullList.create());
		list.add(stack);
		UPGRADES.put(item, list);
	}
	
	public static NonNullList<ItemStack> getUpgrades(@Nonnull Item item)
	{
		return (hasUpgrades(item) ? UPGRADES.get(item) : NonNullList.create());
	}
	
	public static String getUpgradeName(@Nonnull ItemStack stack)
	{
		if (stack.getItem() instanceof IItemUpgrade)
		{
			String name = ((IItemUpgrade) stack.getItem()).getUpgradeName(stack);
			if (name != null && name.length() > 0) return name;
		}
		return stack.getItem().getItemStackDisplayName(stack);
	}
	
	public static String getUpgradeNBT(@Nonnull ItemStack stack)
	{
		if (stack.getItem() instanceof IItemUpgrade)
		{
			return ((IItemUpgrade) stack.getItem()).getUpgradeNBT(stack);
		}
		else if (UPGRADE_NBT.containsKey(stack.getItem()))
		{
			return UPGRADE_NBT.get(stack.getItem());
		}
		return null;
	}
	
	public static void setUpgradeNBT(@Nonnull Item item, @Nonnull String nbt)
	{
		if (nbt.length() > 0) UPGRADE_NBT.put(item, nbt);
	}
	
	public static boolean canApplyUpgrade(@Nonnull ItemStack upgradable, @Nonnull ItemStack upgrade)
	{
		if (hasUpgrades(upgradable.getItem()))
		{
			for (ItemStack stack : getUpgrades(upgradable.getItem()))
			{
				String nbt = getUpgradeNBT(upgrade);
				if (nbt == null) return false;
				if ((upgrade.getItem() instanceof IItemUpgrade && ((IItemUpgrade) upgrade.getItem()).ignoreMeta(upgrade) && upgrade.getItem() == stack.getItem()) || ItemStack.areItemsEqual(stack, upgrade))
				{
					ItemStack result = ItemUtil.getItemFromStack(upgradable, nbt);
					if (result != null && !result.isEmpty()) return false;
					else return true;
				}
			}
		}
		return false;
	}
	
	public static NonNullList<String> getNBTList(@Nonnull Item item)
	{
		NonNullList<String> list = NonNullList.create();
		if (hasUpgrades(item))
		{
			for (ItemStack stack : getUpgrades(item))
			{
				String nbt = getUpgradeNBT(stack);
				if (nbt != null && nbt.length() > 0 && !list.contains(nbt)) list.add(nbt);
			}
		}
		return list;
	}
	
	public static void addUpgradeHandler(@Nonnull Item item, @Nonnull IUpgradeHandler handler)
	{
		HANDLERS.put(item, handler);
	}
	
	public static boolean hasUpgradeHandler(@Nonnull Item item)
	{
		return item instanceof IUpgradeHandler || HANDLERS.containsKey(item);
	}
	
	public static IUpgradeHandler getUpgradeHandler(@Nonnull Item item)
	{
		return (hasUpgradeHandler(item) ? (item instanceof IUpgradeHandler ? (IUpgradeHandler) item : HANDLERS.get(item)) : null);
	}
	
	public static void addUpgrades()
	{
		for (Item item : Item.REGISTRY)
		{
			if (item instanceof IPlatedArmor)
			{
				for (Item plate : Item.REGISTRY)
				{
					if (plate instanceof IArmorPlate)
					{
						UpgradeManager.addUpgrade(item, new ItemStack(plate));
					}
				}
			}
		}
	}
}