package com.nhave.ntechcore.common.content;

import com.nhave.lib.library.helper.WrenchHelper;
import com.nhave.ntechcore.common.integration.top.TOPUpgradeHandler;
import com.nhave.ntechcore.common.itemupgrade.UpgradeManager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModIntegration
{
	public static void postInit(FMLPostInitializationEvent event)
	{
		if (Loader.isModLoaded("theoneprobe"))
		{
			ItemStack probe = GameRegistry.makeItemStack("theoneprobe:probe", 0, 1, null);
			if (!probe.isEmpty())
			{
				UpgradeManager.addUpgrade(ModItems.itemPlateHelmet, probe);
				UpgradeManager.setUpgradeNBT(probe.getItem(), "TOPPROBE");
				UpgradeManager.addUpgradeHandler(probe.getItem(), new TOPUpgradeHandler());
			}
        }
	}
}