package com.nhave.ntechcore.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.nhave.lib.library.helper.ItemNBTHelper;
import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.item.IColoredItem;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IUnifiedItem;
import com.nhave.ntechcore.common.chroma.Chroma;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemChroma extends ItemBase implements IColoredItem
{
	public ItemChroma(String name)
	{
		super(name);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (getChroma(stack) != null) return StringUtils.localize("chroma.ntechcore." + getChroma(stack).getName().toLowerCase()) + " " + super.getItemStackDisplayName(stack);
		else return super.getItemStackDisplayName(stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (!hasChroma(stack))
		{
			tooltip.add(StringUtils.RED + StringUtils.localize("tooltip.ntechcore.error.missingnbt"));
			return;
		}
		
		if (StringUtils.isShiftKeyDown())
		{
			Chroma chroma = getChroma(stack);
			String info = "tooltip.ntechcore.item.chroma." + chroma.getName().toLowerCase();
			if (StringUtils.localize(info).equals(info)) TooltipHelper.addSplitString(tooltip, StringUtils.localize("tooltip.ntechcore.item.chroma").replace("%color%", StringUtils.localize("chroma.ntechcore." + chroma.getName().toLowerCase())), ";", StringUtils.GRAY);
			else TooltipHelper.addSplitString(tooltip, StringUtils.localize(info), ";", StringUtils.GRAY);
			
			if (!Chroma.ITEMS.isEmpty())
			{
				tooltip.add("");
				tooltip.add(StringUtils.localize("tooltip.ntechcore.mod.appliesto") + ":");
				
				List<String> items = new ArrayList<String>();
				for (Item item : chroma.ITEMS)
				{
					ItemStack host = new ItemStack(item);
					String name = item.getItemStackDisplayName(host);
					
					if (item instanceof IUnifiedItem)
					{
						IUnifiedItem U = (IUnifiedItem) item;
						boolean unified = true;
						for (int i = 0; i < U.getFullSet().length; ++i)
						{
							if (!chroma.ITEMS.contains(U.getFullSet()[i])) unified = false;
						}
						if (unified) name = StringUtils.localize(U.getUnifiedName());
					}
					
					if (!items.contains(name))
					{
						items.add(name);
					}
				}
				for (String name : items)
				{
					tooltip.add("  " + StringUtils.format(name, StringUtils.YELLOW, StringUtils.ITALIC));
				}
			}
		}
		else tooltip.add(StringUtils.shiftForInfo());
	}
	
	public boolean hasChroma(ItemStack stack)
	{
		return getChroma(stack) != null;
	}
	
	public Chroma getChroma(ItemStack stack)
	{
		String name = ItemNBTHelper.getString(stack, "CHROMAS", "CHROMA");
		if (name != null && Chroma.CHROMAS.get(name) != null) return Chroma.CHROMAS.get(name);
		return null;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
        {
			if (!Chroma.CHROMAS.isEmpty())
			{
				for(Entry<String, Chroma> entry : Chroma.CHROMAS.entrySet())
				{
					String key = entry.getKey();
					items.add(ItemNBTHelper.setString(new ItemStack(this), "CHROMAS", "CHROMA", key));
				}
			}
			//else items.add(new ItemStack(this));
        }
	}
	
	@Override
	public int getQuality(ItemStack stack)
	{
		if (hasChroma(stack))
		{
			return getChroma(stack).getQuality();
		}
		return super.getQuality(stack);
	}
	
	@Override
	public int getItemColor(ItemStack stack, int index)
	{
		if (hasChroma(stack) && index == 1) return getChroma(stack).getColor();
		return 16777215;
	}
}