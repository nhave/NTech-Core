package com.nhave.ntechcore.core.registry;

import com.nhave.ntechcore.common.content.ModBlocks;
import com.nhave.ntechcore.common.content.ModItems;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientRegistryHandler
{
	@SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event)
	{
		ModBlocks.registerRenders();
		ModItems.registerRenders();
	}
}