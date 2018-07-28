package com.nhave.ntechcore.client.mesh;

import com.nhave.ntechcore.common.armorplate.ArmorPlate;
import com.nhave.ntechcore.common.item.ItemArmorPlate;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomMeshDefinitionArmorPlate implements ItemMeshDefinition
{
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		ItemArmorPlate item = (ItemArmorPlate) stack.getItem();
		if (!ArmorPlate.PLATES.isEmpty())
		{
			if (item.hasPlate(stack))
			{
				ResourceLocation resource = item.getResourceLocation(stack);
				return new ModelResourceLocation(new ResourceLocation(resource.getResourceDomain(), "armorplates/" + item.getItemName() + "_" + resource.getResourcePath()), "inventory");
			}
		}
		return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
	}
}
