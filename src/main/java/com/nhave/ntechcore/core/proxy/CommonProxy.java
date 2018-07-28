package com.nhave.ntechcore.core.proxy;

import java.io.File;

import com.nhave.ntechcore.common.content.ModConfig;
import com.nhave.ntechcore.common.eventhandler.ArmorEventHandler;
import com.nhave.ntechcore.common.eventhandler.UpgradeEventHandler;
import com.nhave.ntechcore.core.registry.RegistryHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void setupConfig(File configFile)
	{
		MinecraftForge.EVENT_BUS.register(new ModConfig(false));
		ModConfig.init(configFile);
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new RegistryHandler());
	}
	
	public void registerRenders() {}
	
	public void registerEventHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new UpgradeEventHandler());
		MinecraftForge.EVENT_BUS.register(new ArmorEventHandler());
	}
}