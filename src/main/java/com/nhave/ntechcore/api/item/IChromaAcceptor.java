package com.nhave.ntechcore.api.item;

import net.minecraft.item.ItemStack;

public interface IChromaAcceptor
{
	public boolean supportsChroma(ItemStack stack);
}