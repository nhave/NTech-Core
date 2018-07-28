package com.nhave.ntechcore.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.api.item.armor.IMultiJump;
import com.nhave.ntechcore.api.item.armor.IStepAssist;
import com.nhave.ntechcore.common.itemupgrade.UpgradeHelper;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRunningBoots extends ItemArmorBase implements IStepAssist, IMultiJump
{
	protected static final UUID[] SPEED_MODIFIERS = new UUID[]{
            UUID.fromString("857af40b-def8-47e3-a838-527933eca586"),
            UUID.fromString("1fd2c8fb-5a76-4e6f-bccd-c27f2287ad1b"),
            UUID.fromString("54e47051-19ac-4e59-9e54-4f256484408a"),
            UUID.fromString("ac16532f-d0b0-4b23-a89a-85ecaa3b5d7d") };
	
	public ItemRunningBoots(String name, ArmorMaterial materialIn)
	{
		super(name, materialIn, 0, EntityEquipmentSlot.FEET);
		this.setQuality(3);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (StringUtils.isShiftKeyDown())
		{
			TooltipHelper.addSplitString(tooltip, StringUtils.localize("tooltip.ntechcore.item." + getItemName()), ";", StringUtils.GRAY);
		}
		else tooltip.add(StringUtils.shiftForInfo());
		UpgradeHelper.addUpgradeInfo(stack, world, tooltip, flag);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> attributeMap = HashMultimap.<String, AttributeModifier>create();
		if (slot == this.armorType) attributeMap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(SPEED_MODIFIERS[slot.getIndex()], "Speedy modifier", 0.2F, 2));
		return attributeMap;
	}
	
	@Override
	public int getInAirJumps(ItemStack stack)
	{
		ItemStack jumpkit = ItemUtil.getItemFromStack(stack, "JUMPKIT");
		if (jumpkit != null && jumpkit.getItem() instanceof ItemJumpUpgrade)
		{
			ItemJumpUpgrade upgrade = ((ItemJumpUpgrade) jumpkit.getItem());
			return upgrade.getInAirJumps(jumpkit);
		}
		return 0;
	}
	
	@Override
	public float getJumpHeight(ItemStack stack)
	{
		ItemStack jumpkit = ItemUtil.getItemFromStack(stack, "JUMPKIT");
		if (jumpkit != null && jumpkit.getItem() instanceof ItemJumpUpgrade)
		{
			ItemJumpUpgrade upgrade = ((ItemJumpUpgrade) jumpkit.getItem());
			return upgrade.getJumpHeight(jumpkit);
		}
		return 0;
	}

	@Override
	public float getJumpVelocity(ItemStack stack)
	{
		ItemStack jumpkit = ItemUtil.getItemFromStack(stack, "JUMPKIT");
		if (jumpkit != null && jumpkit.getItem() instanceof ItemJumpUpgrade)
		{
			ItemJumpUpgrade upgrade = ((ItemJumpUpgrade) jumpkit.getItem());
			return upgrade.getJumpVelocity(jumpkit);
		}
		return 0;
	}
	
	@Override
	public void onJump(ItemStack stack) {}
}