package com.nhave.ntechcore.common.item;

import com.nhave.ntechcore.common.content.ModCreativeTabs;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.core.Reference;
import com.nhave.rpgtooltips.common.item.EnumItemQuality;
import com.nhave.rpgtooltips.common.item.IItemQuality;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
    @Optional.Interface(iface = "com.nhave.rpgtooltips.common.item.IItemQuality", modid = "rpgtooltips")
})
public class ItemArmorBase extends ItemArmor implements IItemQuality
{
	private int quality = 0;
	private String armorTexture;
	
	public ItemArmorBase(String name, String armorTexture, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		this.setRegistryName(name);
		this.setCreativeTab(ModCreativeTabs.getArmor());
		this.setUnlocalizedName(Reference.MODID + "." + name);
		this.armorTexture = armorTexture;
		
		ModItems.addItem(this);
	}
	
	public ItemArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		this(name, name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	public String getItemName()
	{
		return this.getRegistryName().getResourcePath();
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot equipmentSlot, String armorTexture)
	{
		return Reference.MODID + ":textures/models/armor/" + this.armorTexture + ".png";
	}
	
	public ItemArmorBase setQuality(int quality)
	{
		this.quality = quality;
		return this;
	}
	
	public int getQuality(ItemStack stack)
	{
		return this.quality;
	}
	
	@Optional.Method(modid = "rpgtooltips")
	@Override
	public EnumItemQuality getItemQuality(ItemStack stack)
	{
		switch (getQuality(stack))
		{
		case 0:
			return EnumItemQuality.COMMON;
		case 1:
			return EnumItemQuality.UNCOMMON;
		case 2:
			return EnumItemQuality.RARE;
		case 3:
			return EnumItemQuality.EPIC;
		case 4:
			return EnumItemQuality.LEGENDARY;
		case 5:
			return EnumItemQuality.RELIC;
		case 6:
			return EnumItemQuality.CREATIVE;
		default:
			return EnumItemQuality.COMMON;
		}
	}
}