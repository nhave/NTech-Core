package com.nhave.ntechcore.common.armorplate;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class ArmorPlate
{
	public static final Map<String, ArmorPlate> PLATES = Maps.newHashMap();
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	private String name;
	private String resourceDomain;
	private ArmorMaterial material;
	private int quality;
	
	public ArmorPlate(String name, String modid, ArmorMaterial material, int quality)
	{
		this.name = name;
		this.resourceDomain = modid;
		this.material = material;
		this.quality = quality;
		
		PLATES.put(name, this);
	}
	
	public ArmorPlate(String name, ArmorMaterial material, int quality)
	{
		this(name, Reference.MODID, material, quality);
	}
	
	public ArmorPlate(String name, ArmorMaterial material)
	{
		this(name, Reference.MODID, material, 0);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getResourceDomain()
	{
		return this.resourceDomain;
	}
	
	public int getQuality()
	{
		return this.quality;
	}
	
	public int getDamageReduction(EntityEquipmentSlot slot)
	{
		return this.material.getDamageReductionAmount(slot);
	}
	
	public float getToughness()
	{
		return this.material.getToughness();
	}
	
	public void addAttributeModifiers(Multimap<String, AttributeModifier> attributeMap, EntityEquipmentSlot slot)
	{
		attributeMap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", (double)this.material.getDamageReductionAmount(slot), 0));
		attributeMap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", (double)this.material.getToughness(), 0));
	}
}