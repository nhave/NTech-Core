package com.nhave.ntechcore.api.item.armor;

import net.minecraft.item.ItemStack;

public interface IMultiJump
{
	/**
	 * Gets the number of additional jumps the player can do mid air.
	 * 
	 * @param stack The item providing mid air jumps.
	 * @return The number of additional jumps the player can do.
	 */
	public int getInAirJumps(ItemStack stack);
	
	/**
	 * Gets the height gained from jumping mid air.
	 * 
	 * @param stack The item providing mid air jumps.
	 * @return The height gained from jumping.
	 */
	public float getJumpHeight(ItemStack stack);
	
	/**
	 * Gets the velocity gained from jumping mid air.
	 * 
	 * @param stack The item providing mid air jumps.
	 * @return The velocity gained from jumping.
	 */
	public float getJumpVelocity(ItemStack stack);
	
	/**
	 * Called when the player jumps in mid air.
	 * Can be used to draw power from the item.
	 * 
	 * @param stack The item providing mid air jumps.
	 */
	public void onJump(ItemStack stack);
}