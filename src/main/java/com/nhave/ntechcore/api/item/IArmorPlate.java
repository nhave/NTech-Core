package com.nhave.ntechcore.api.item;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public interface IArmorPlate
{
	public void addAttributeModifiers(Multimap<String, AttributeModifier> attributeMap, EntityEquipmentSlot slot, ItemStack stack);
}