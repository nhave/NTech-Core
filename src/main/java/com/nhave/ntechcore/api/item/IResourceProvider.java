package com.nhave.ntechcore.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IResourceProvider
{
	public ResourceLocation getResourceLocation(ItemStack stack);
}