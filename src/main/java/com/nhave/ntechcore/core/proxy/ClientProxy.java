package com.nhave.ntechcore.core.proxy;

import java.io.File;

import com.nhave.ntechcore.client.eventhandler.ClientArmorEventHandler;
import com.nhave.ntechcore.client.eventhandler.TooltipEventHandler;
import com.nhave.ntechcore.common.content.ModConfig;
import com.nhave.ntechcore.core.registry.ClientRegistryHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void setupConfig(File configFile)
	{
		MinecraftForge.EVENT_BUS.register(new ModConfig(true));
		ModConfig.init(configFile);
	}
	
	@Override
    public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ClientRegistryHandler());
    }
	
	@Override
	public void registerRenders() {}
	
	@Override
	public void registerEventHandlers()
	{
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
		MinecraftForge.EVENT_BUS.register(new ClientArmorEventHandler());
	}
}