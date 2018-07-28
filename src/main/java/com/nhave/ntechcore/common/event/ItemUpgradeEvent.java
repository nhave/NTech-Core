package com.nhave.ntechcore.common.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemUpgradeEvent extends Event
{
	private final ItemStack input;
	private final ItemStack upgrade;
	private ItemStack output;
	private int materialCost;
	private int craftingTime;

	public ItemUpgradeEvent(ItemStack input, ItemStack upgrade)
	{
		this.input = input;
		this.upgrade = upgrade;
		this.materialCost = 1;
		this.craftingTime = 100;
	}
	
	public ItemStack getInput()
	{
		return this.input;
	}
	
	public ItemStack getUpgrade()
	{
		return this.upgrade;
	}
	
	public ItemStack getOutput()
	{
		return this.output;
	}
	
	public int getMaterialCost()
	{
		return this.materialCost;
	}
	
	public int getCraftingTime()
	{
		return this.craftingTime;
	}
	
	public void setOutput(ItemStack output)
	{
		this.output = output;
	}
	
	public void setMaterialCost(int materialCost)
	{
		this.materialCost = materialCost;
	}
	
	public void setCraftingTime(int craftingTime)
	{
		this.craftingTime = craftingTime;
	}
}