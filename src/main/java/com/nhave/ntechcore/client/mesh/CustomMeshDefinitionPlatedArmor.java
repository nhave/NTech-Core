package com.nhave.ntechcore.client.mesh;

import com.nhave.lib.library.util.ItemUtil;
import com.nhave.ntechcore.api.item.IResourceProvider;
import com.nhave.ntechcore.common.item.ItemPlatedArmor;
import com.nhave.ntechcore.common.itemupgrade.UpgradeHelper;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomMeshDefinitionPlatedArmor implements ItemMeshDefinition
{
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		ItemPlatedArmor item = (ItemPlatedArmor) stack.getItem();
		if (UpgradeHelper.hasArmorPlate(stack))
		{
			ItemStack armorplate = ItemUtil.getItemFromStack(stack, "ARMORPLATE");
			
			if (armorplate.getItem() instanceof IResourceProvider)
			{
				ResourceLocation provider = ((IResourceProvider) armorplate.getItem()).getResourceLocation(armorplate);
				if (provider != null)
				{
					return new ModelResourceLocation(new ResourceLocation(provider.getResourceDomain(), "armorplates/" + item.getItemName() + "_" + provider.getResourcePath()), "inventory");
				}
			}
		}
		return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
	}
}