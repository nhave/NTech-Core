package com.nhave.ntechcore.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.nhave.lib.library.helper.ItemNBTHelper;
import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.item.IColoredItem;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IResourceProvider;
import com.nhave.ntechcore.api.item.IUnifiedItem;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.common.itemshader.Shader;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemShader extends ItemBase implements IColoredItem, IResourceProvider
{
	public ItemShader(String name)
	{
		super(name);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (hasShader(stack)) return StringUtils.localize("shader." + getShader(stack).getResourceDomain() + "." + getShader(stack).getName());
		return super.getItemStackDisplayName(stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (!hasShader(stack))
		{
			tooltip.add(StringUtils.RED + StringUtils.localize("tooltip.ntechcore.error.missingnbt"));
			return;
		}
		else if (StringUtils.isShiftKeyDown())
		{
			Shader shader = getShader(stack);
			
			tooltip.add(StringUtils.format(super.getItemStackDisplayName(stack), StringUtils.GREEN, StringUtils.ITALIC));
			TooltipHelper.addHiddenTooltip(tooltip, "tooltip." + shader.getResourceDomain() + ".shader." + shader.getName(), ";", StringUtils.GRAY);
			
			if (!shader.getCompatibleItems().isEmpty())
			{
				tooltip.add("");
				tooltip.add(StringUtils.localize("tooltip.ntechcore.mod.appliesto") + ":");
				
				List<String> items = new ArrayList<String>();
				for (Item item : shader.getCompatibleItems())
				{
					ItemStack host = new ItemStack(item);
					String name = item.getItemStackDisplayName(host);
					
					if (item instanceof IUnifiedItem)
					{
						IUnifiedItem U = (IUnifiedItem) item;
						boolean unified = true;
						for (int i = 0; i < U.getFullSet().length; ++i)
						{
							if (!shader.getCompatibleItems().contains(U.getFullSet()[i])) unified = false;
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
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
        {
			if (!Shader.SHADERS.isEmpty())
			{
				for(Entry<String, Shader> entry : Shader.SHADERS.entrySet())
				{
					String key = entry.getKey();
					items.add(ItemNBTHelper.setString(new ItemStack(this), "SHADERS", "SHADER", key));
				}
			}
        }
	}
	
	public Shader getShader(ItemStack stack)
	{
		String name = ItemNBTHelper.getString(stack, "SHADERS", "SHADER");
		if (name != null && Shader.SHADERS.get(name) != null) return Shader.SHADERS.get(name);
		return null;
	}
	
	private boolean hasShader(ItemStack stack)
	{
		return getShader(stack) != null;
	}
	
	@Override
	public ResourceLocation getResourceLocation(ItemStack stack)
	{
		if (hasShader(stack))
		{
			return new ResourceLocation(getShader(stack).getResourceDomain(), getShader(stack).getName());
		}
		return null;
	}
	
	public boolean canApplyShader(ItemStack shadeable, ItemStack shader)
	{
		if (hasShader(shader) && getShader(shader).isItemCompatible(shadeable.getItem())) 
		{
			ItemStack shaderB = ItemUtil.getItemFromStack(shadeable, "SHADER");
			if (shaderB != null && shaderB.getItem() == ModItems.itemShader)
			{
				if (shaderB.getItem() == this && getShader(shader) == getShader(shaderB))
				{
					return false;
				}
			}
			return getShader(shader).isItemCompatible(shadeable.getItem());
		}
		return false;
	}
	
	@Override
	public int getQuality(ItemStack stack)
	{
		if (hasShader(stack))
		{
			return getShader(stack).getQuality();
		}
		return super.getQuality(stack);
	}
	
	@Override
	public int getItemColor(ItemStack stack, int index)
	{
		if (hasShader(stack) && (index == 1 || index == 2)) return getShader(stack).getColor(index-1);
		return 16777215;
	}
}