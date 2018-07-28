package com.nhave.ntechcore.common.eventhandler;

import com.nhave.lib.library.helper.ItemNBTHelper;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.ntechcore.api.item.IChromaAcceptor;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.common.event.ItemUpgradeEvent;
import com.nhave.ntechcore.common.item.ItemChroma;
import com.nhave.ntechcore.common.itemupgrade.UpgradeManager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UpgradeEventHandler
{
	@SubscribeEvent()
	public void onItemUpgrade(ItemUpgradeEvent evt)
    {
		if (evt.getInput().isEmpty() || evt.getUpgrade().isEmpty())
		{
			return;
		}
		
		if (UpgradeManager.canApplyUpgrade(evt.getInput(), evt.getUpgrade()))
		{
			ItemStack output = evt.getInput().copy();
			ItemStack upgrade = evt.getUpgrade().copy();
			upgrade.setCount(1);
			ItemUtil.addItemToStack(output, upgrade, UpgradeManager.getUpgradeNBT(upgrade));
			if (UpgradeManager.hasUpgradeHandler(upgrade.getItem()))
			{
				UpgradeManager.getUpgradeHandler(upgrade.getItem()).onUpgradeApplied(output, upgrade);
			}
			evt.setOutput(output);
			evt.setMaterialCost(1);
			return;
		}
		if (evt.getInput().getItem() instanceof IChromaAcceptor && evt.getUpgrade().getItem() == ModItems.itemChroma)
		{
			if (((IChromaAcceptor) evt.getInput().getItem()).supportsChroma(evt.getInput()))
			{
				ItemStack stackChroma = ItemUtil.getItemFromStack(evt.getInput(), "CHROMA");
				if (stackChroma == null) stackChroma = ItemNBTHelper.setString(new ItemStack(ModItems.itemChroma), "CHROMAS", "CHROMA", "white");
				
				if (stackChroma.getItem() == ModItems.itemChroma)
				{
					ItemChroma itemChroma = (ItemChroma) stackChroma.getItem();
					if (itemChroma.getChroma(stackChroma) == itemChroma.getChroma(evt.getUpgrade())) return;
				}
				
				ItemStack output = evt.getInput().copy();
				ItemStack chroma = evt.getUpgrade().copy();
				chroma.setCount(1);
				ItemUtil.addItemToStack(output, chroma, "CHROMA");
				evt.setOutput(output);
				evt.setMaterialCost(1);
			}
			return;
		}
    }
	
	@SubscribeEvent()
	public void onAnvilUpdate(AnvilUpdateEvent evt)
    {
		/*if (evt.getLeft().isEmpty() || evt.getRight().isEmpty())
		{
			return;
		}
		
		if (UpgradeManager.canApplyUpgrade(evt.getLeft(), evt.getRight()))
		{
			ItemStack output = evt.getLeft().copy();
			ItemStack upgrade = evt.getRight().copy();
			upgrade.setCount(1);
			ItemUtil.addItemToStack(output, upgrade, UpgradeManager.getUpgradeNBT(upgrade));
			if (UpgradeManager.hasUpgradeHandler(upgrade.getItem()))
			{
				UpgradeManager.getUpgradeHandler(upgrade.getItem()).onUpgradeApplied(output, upgrade);
			}
			evt.setOutput(output);
			evt.setMaterialCost(1);
			evt.setCost(1);
			return;
		}
		if (evt.getLeft().getItem() instanceof IChromaAcceptor && evt.getRight().getItem() == ModItems.itemChroma)
		{
			if (((IChromaAcceptor) evt.getLeft().getItem()).supportsChroma(evt.getLeft()))
			{
				ItemStack stackChroma = ItemUtil.getItemFromStack(evt.getLeft(), "CHROMA");
				if (stackChroma == null) stackChroma = ItemNBTHelper.setString(new ItemStack(ModItems.itemChroma), "CHROMAS", "CHROMA", "white");
				
				if (stackChroma.getItem() == ModItems.itemChroma)
				{
					ItemChroma itemChroma = (ItemChroma) stackChroma.getItem();
					if (itemChroma.getChroma(stackChroma) == itemChroma.getChroma(evt.getRight())) return;
				}
				
				ItemStack output = evt.getLeft().copy();
				ItemStack chroma = evt.getRight().copy();
				chroma.setCount(1);
				ItemUtil.addItemToStack(output, chroma, "CHROMA");
				evt.setOutput(output);
				evt.setMaterialCost(1);
				evt.setCost(1);
			}
			return;
		}*/
    }
}