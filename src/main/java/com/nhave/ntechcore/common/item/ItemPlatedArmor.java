package com.nhave.ntechcore.common.item;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.nhave.lib.library.client.render.IModelRegister;
import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.IArmorPlate;
import com.nhave.ntechcore.api.item.IResourceProvider;
import com.nhave.ntechcore.api.item.IUnifiedItem;
import com.nhave.ntechcore.api.item.armor.IPlatedArmor;
import com.nhave.ntechcore.client.mesh.CustomMeshDefinitionPlatedArmor;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.common.itemupgrade.UpgradeHelper;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPlatedArmor extends ItemArmorBase implements IModelRegister, IPlatedArmor, IUnifiedItem
{
	public ItemPlatedArmor(String name, ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(name, "plate" + (equipmentSlotIn == EntityEquipmentSlot.LEGS ? "_2" : "_1"), materialIn, 0, equipmentSlotIn);
		this.setQuality(2);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (StringUtils.isShiftKeyDown())
		{
			TooltipHelper.addSplitString(tooltip, StringUtils.localize("tooltip.ntechcore.item.platearmor"), ";", StringUtils.GRAY);
		}
		else tooltip.add(StringUtils.shiftForInfo());
		UpgradeHelper.addUpgradeInfo(stack, world, tooltip, flag);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (UpgradeHelper.hasArmorPlate(stack))
		{
			ItemStack armorplate = ItemUtil.getItemFromStack(stack, "ARMORPLATE");
			
			if (armorplate.getItem() instanceof IResourceProvider)
			{
				ResourceLocation provider = ((IResourceProvider) armorplate.getItem()).getResourceLocation(armorplate);

				return StringUtils.localize("armorplate." + provider.getResourceDomain() + "." + provider.getResourcePath()) + " " + super.getItemStackDisplayName(stack);
			}
		}
		return super.getItemStackDisplayName(stack);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot equipmentSlot, String armorTexture)
	{
		if (UpgradeHelper.hasArmorPlate(stack))
		{
			ItemStack armorplate = ItemUtil.getItemFromStack(stack, "ARMORPLATE");
			
			if (armorplate.getItem() instanceof IResourceProvider)
			{
				ResourceLocation provider = ((IResourceProvider) armorplate.getItem()).getResourceLocation(armorplate);
				if (provider != null)
				{
					return provider.getResourceDomain() + ":textures/models/armor/armorplates/plate_" + provider.getResourcePath() + (equipmentSlot == EntityEquipmentSlot.LEGS ? "_2" : "_1") + ".png";
				}
			}
		}
		return super.getArmorTexture(stack, entity, equipmentSlot, armorTexture);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> attributeMap = HashMultimap.<String, AttributeModifier>create();
		
		if (slot == this.armorType && UpgradeHelper.hasArmorPlate(stack))
		{
			ItemStack armorplate = ItemUtil.getItemFromStack(stack, "ARMORPLATE");
			IArmorPlate plate = (IArmorPlate) armorplate.getItem();
			plate.addAttributeModifiers(attributeMap, slot, armorplate);
		}
		
		return attributeMap;
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModels()
	{
		ModelLoader.registerItemVariants(this, new ResourceLocation(Reference.MODID + ":" + this.getItemName()));
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelLoader.setCustomMeshDefinition(this, new CustomMeshDefinitionPlatedArmor());
	}

	@Override
	public String getUnifiedName()
	{
		return "tooltip.ntechcore.item.plate.all";
	}

	@Override
	public Item[] getFullSet()
	{
		return new Item[] {ModItems.itemPlateHelmet, ModItems.itemPlateChest, ModItems.itemPlateLegs, ModItems.itemPlateBoots};
	}
}