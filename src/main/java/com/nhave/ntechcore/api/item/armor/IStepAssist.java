package com.nhave.ntechcore.api.item.armor;

import net.minecraft.item.ItemStack;

public interface IStepAssist
{
	/**
	 * Allows boots to provide Step Assist.
	 * To disable return 0.6F of less.
	 * 
	 * @param stack The item providing Step Assist.
	 * @return The height the player can step up.
	 */
	public default float getStepHeight(ItemStack stack)
	{
		return 1.25F;
	}
}