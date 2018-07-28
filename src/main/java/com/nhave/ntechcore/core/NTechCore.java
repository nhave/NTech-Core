package com.nhave.ntechcore.core;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.nhave.ntechcore.common.chroma.Chroma;
import com.nhave.ntechcore.common.content.ModBlocks;
import com.nhave.ntechcore.common.content.ModConfig;
import com.nhave.ntechcore.common.content.ModIntegration;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.common.itemupgrade.UpgradeManager;
import com.nhave.ntechcore.common.network.PacketHandler;
import com.nhave.ntechcore.core.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, guiFactory = Reference.GUIFACTORY)
public class NTechCore
{
	public static final ModItems ITEMS = ModItems.INSTANCE;
	public static final ModBlocks BLOCKS = ModBlocks.INSTANCE;
	public static final ModConfig CONFIG = ModConfig.INSTANCE;
	
	public static Logger logger;
    
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
	public static CommonProxy proxy;
    
    @Mod.Instance(Reference.MODID)
	public static NTechCore instance = new NTechCore();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
		proxy.setupConfig(new File(event.getModConfigurationDirectory(), "ntechcore.cfg"));
    	proxy.preInit(event);
    	
		PacketHandler.init();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerRenders();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.registerEventHandlers();
    	
    	ModIntegration.postInit(event);
    	UpgradeManager.addUpgrades();
    	Chroma.createList();
    }
}