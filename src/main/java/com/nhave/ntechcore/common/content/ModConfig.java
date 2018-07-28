package com.nhave.ntechcore.common.content;

import java.io.File;

import com.nhave.ntechcore.core.Reference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModConfig
{
	public static final ModConfig INSTANCE = new ModConfig(false);
	
	public static boolean isClientConfig;
	
	public static Configuration config;
	
	public ModConfig(boolean isClient)
	{
		this.isClientConfig = isClient;
	}

	public static void init(File configFile)
	{
		config = new Configuration(configFile);
		loadConfig(false);
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
	{
		if(eventArgs.getModID().equalsIgnoreCase(Reference.MODID))
		{
			loadConfig(false);
		}
	}
	
	public static void loadConfig(boolean load)
	{
		loadCommonConfig();
		if (isClientConfig) loadClientConfig();
		
		if (!config.hasChanged()) return;
		config.save();
	}
	
	public static void loadCommonConfig()
	{
		//config.setCategoryComment("common", "Configuration for all Common configs");
	}
	
	public static void loadClientConfig()
	{
		//config.setCategoryComment("client", "Configuration for all Client configs");
	}
}