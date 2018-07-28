package com.nhave.ntechcore.common.item;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;
import com.nhave.lib.library.client.render.IModelRegister;
import com.nhave.lib.library.helper.ItemNBTHelper;
import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IArmorPlate;
import com.nhave.ntechcore.api.item.IItemUpgrade;
import com.nhave.ntechcore.api.item.IResourceProvider;
import com.nhave.ntechcore.client.mesh.CustomMeshDefinitionArmorPlate;
import com.nhave.ntechcore.common.armorplate.ArmorPlate;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorPlate extends ItemBase implements IModelRegister, IArmorPlate, IItemUpgrade, IResourceProvider
{
	public ItemArmorPlate(String name)
	{
		super(name);
		this.setHasSubtypes(true);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (!hasPlate(stack))
		{
			tooltip.add(StringUtils.RED + StringUtils.localize("tooltip.ntechcore.error.missingnbt"));
			return;
		}
		
		if (StringUtils.isShiftKeyDown())
		{
			TooltipHelper.addSplitString(tooltip, StringUtils.localize("tooltip.ntechcore.item." + getItemName()), ";", StringUtils.GRAY);
			
			tooltip.add("");
			tooltip.add(StringUtils.localize("tooltip.ntechcore.armorplate.onarmor") + ":");
			
			int toughness = (int) getPlate(stack).getToughness();
			if (toughness > 0) tooltip.add(StringUtils.format(" +" + toughness + " " + StringUtils.localize("tooltip.ntechcore.armorplate.toughness"), StringUtils.LIGHT_BLUE));
			
			tooltip.add(StringUtils.format(" +" + getPlate(stack).getDamageReduction(EntityEquipmentSlot.HEAD) + " " + StringUtils.localize("tooltip.ntechcore.armorplate.onhead"), StringUtils.LIGHT_BLUE));
			tooltip.add(StringUtils.format(" +" + getPlate(stack).getDamageReduction(EntityEquipmentSlot.CHEST) + " " + StringUtils.localize("tooltip.ntechcore.armorplate.onbody"), StringUtils.LIGHT_BLUE));
			tooltip.add(StringUtils.format(" +" + getPlate(stack).getDamageReduction(EntityEquipmentSlot.LEGS) + " " + StringUtils.localize("tooltip.ntechcore.armorplate.onlegs"), StringUtils.LIGHT_BLUE));
			tooltip.add(StringUtils.format(" +" + getPlate(stack).getDamageReduction(EntityEquipmentSlot.FEET) + " " + StringUtils.localize("tooltip.ntechcore.armorplate.onfeet"), StringUtils.LIGHT_BLUE));
		}
		else tooltip.add(StringUtils.shiftForInfo());
	}
	
	public ArmorPlate getPlate(ItemStack stack)
	{
		String name = ItemNBTHelper.getString(stack, "ARMORPLATE", "ARMORPLATE");
		if (name != null && ArmorPlate.PLATES.get(name) != null) return ArmorPlate.PLATES.get(name);
		return null;
	}
	
	public boolean hasPlate(ItemStack stack)
	{
		return getPlate(stack) != null;
	}
	
	@Override
	public void addAttributeModifiers(Multimap<String, AttributeModifier> attributeMap, EntityEquipmentSlot slot, ItemStack stack)
	{
		if (hasPlate(stack))
		{
			getPlate(stack).addAttributeModifiers(attributeMap, slot);
		}
	}
	
	@Override
	public ResourceLocation getResourceLocation(ItemStack stack)
	{
		if (hasPlate(stack))
		{
			return new ResourceLocation(getPlate(stack).getResourceDomain(), getPlate(stack).getName());
		}
		return null;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
        {
			if (!ArmorPlate.PLATES.isEmpty())
			{
				for(Entry<String, ArmorPlate> entry : ArmorPlate.PLATES.entrySet())
				{
					String key = entry.getKey();
					items.add(ItemNBTHelper.setString(new ItemStack(this), "ARMORPLATE", "ARMORPLATE", key));
				}
			}
			//else items.add(new ItemStack(this));
        }
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (hasPlate(stack)) return StringUtils.localize("armorplate." + getPlate(stack).getResourceDomain() + "." + getPlate(stack).getName()) + " " + super.getItemStackDisplayName(stack);
		else return super.getItemStackDisplayName(stack);
	}
	
	@Override
	public int getQuality(ItemStack stack)
	{
		if (hasPlate(stack))
		{
			return getPlate(stack).getQuality();
		}
		return super.getQuality(stack);
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModels()
	{
		ModelLoader.registerItemVariants(this, new ResourceLocation(Reference.MODID + ":" + this.getRegistryName().getResourcePath()));
		if (!ArmorPlate.PLATES.isEmpty())
		{
			for(Entry<String, ArmorPlate> entry : ArmorPlate.PLATES.entrySet())
			{
				String key = entry.getKey();
				ArmorPlate plate = entry.getValue();
				ModelLoader.registerItemVariants(this, new ResourceLocation(plate.getResourceDomain() + ":armorplates/" + this.getItemName() + "_" + key.toLowerCase()));
				ModelLoader.registerItemVariants(ModItems.itemPlateHelmet, new ResourceLocation(plate.getResourceDomain() + ":armorplates/platehelmet_" + key.toLowerCase()));
				ModelLoader.registerItemVariants(ModItems.itemPlateChest, new ResourceLocation(plate.getResourceDomain() + ":armorplates/platechest_" + key.toLowerCase()));
				ModelLoader.registerItemVariants(ModItems.itemPlateLegs, new ResourceLocation(plate.getResourceDomain() + ":armorplates/platelegs_" + key.toLowerCase()));
				ModelLoader.registerItemVariants(ModItems.itemPlateBoots, new ResourceLocation(plate.getResourceDomain() + ":armorplates/plateboots_" + key.toLowerCase()));
			}
		}
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelLoader.setCustomMeshDefinition(this, new CustomMeshDefinitionArmorPlate());
	}
	
	@Override
	public String getUpgradeNBT(ItemStack upgrade)
	{
		return "ARMORPLATE";
	}
	
	@Override
	public boolean ignoreMeta(ItemStack upgrade)
	{
		return true;
	}
	
	@Override
	public String getUpgradeName(ItemStack upgrade)
	{
		return  StringUtils.localize("tooltip.ntechcore.mod.armorplate");
	}
}