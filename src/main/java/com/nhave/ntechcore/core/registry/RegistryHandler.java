package com.nhave.ntechcore.core.registry;

import com.nhave.ntechcore.common.content.ModBlocks;
import com.nhave.ntechcore.common.content.ModItems;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RegistryHandler
{
	@SubscribeEvent
    public void onBlockRegistry(Register<Block> event)
	{
		ModBlocks.init();
		ModBlocks.register(event);
    }
	
	@SubscribeEvent
    public void onItemRegistry(Register<Item> event)
    {
		ModBlocks.registerItemBlocks(event);
		ModItems.init();
		ModItems.register(event);
    }
    
    @SubscribeEvent
    public void onCraftingRegistry(Register<IRecipe> event)
    {
//    	ModCrafting.register(event);
    }
}