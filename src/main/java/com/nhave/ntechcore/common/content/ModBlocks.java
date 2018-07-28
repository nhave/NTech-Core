package com.nhave.ntechcore.common.content;

import java.util.ArrayList;
import java.util.List;

import com.nhave.lib.library.client.render.IModelRegister;
import com.nhave.lib.library.item.IItemBlockRegister;
import com.nhave.lib.library.item.IOreRegister;
import com.nhave.lib.library.tile.ITileRegister;
import com.nhave.ntechcore.common.block.BlockMachineBase;
import com.nhave.ntechcore.common.block.BlockMachineHorizontal;
import com.nhave.ntechcore.common.block.BlockMachineIO;
import com.nhave.ntechcore.common.block.BlockUpgradeStation;
import com.nhave.ntechcore.common.itemblock.ItemBlockBase;
import com.nhave.ntechcore.core.NTechCore;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks
{
	public static final ModBlocks INSTANCE = new ModBlocks();
	
	private static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static Block blockFrame;
	public static Block blockIO;
	public static Block blockUpgrade;
	public static Block blockFabricator;
	//public static Block blockCosmetics;
	
	public static void init()
	{
		blockFrame = new BlockMachineBase("machineframe").setQuality(1);
		blockIO = new BlockMachineIO("io").setQuality(2);
		blockUpgrade = new BlockUpgradeStation("upgradestation").setMaxStoredPower(20000).setQuality(2);
		blockFabricator = new BlockMachineHorizontal("fabricator").setMaxStoredPower(20000).setQuality(2);
		//blockCosmetics = new BlockMachineBase("cosmetics").setQuality(2);
		
		ModCreativeTabs.getItems().setTabIconItem(new ItemStack(blockFrame));
	}
	
	public static Block addBlock(Block block)
	{
		BLOCKS.add(block);
		return block;
	}
	
	public static List<Block> getBlocks()
	{
		return BLOCKS;
	}
	
	public static void register(Register<Block> event)
	{
		for (Block block : getBlocks())
		{
			NTechCore.logger.info("Registering Block: " + block.getRegistryName());
			event.getRegistry().register(block);
			if (block instanceof IOreRegister) ((IOreRegister) block).registerOres();
			if (block instanceof ITileRegister) ((ITileRegister) block).registerTileEntity();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		for (Block block : getBlocks())
		{
			NTechCore.logger.info("Registering Renderers for Block: " + block.getRegistryName());
			if (block instanceof IModelRegister) ((IModelRegister) block).registerModels();
			else registerRender(block);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block)
	{
		Item item = Item.getItemFromBlock(block);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getRegistryName().getResourcePath(), "normal"));
	}
	
	public static void registerItemBlocks(Register<Item> event)
	{
		for (Block block : getBlocks())
		{
			NTechCore.logger.info("Registering ItemBlock for Block: " + block.getRegistryName());
			if (block instanceof IItemBlockRegister) ((IItemBlockRegister) block).registerItemBlock(event);
			else registerItemBlock(event, block);
		}
	}
	
	public static void registerItemBlock(Register<Item> event, Block block)
	{
		event.getRegistry().register(new ItemBlockBase(block).setRegistryName(block.getRegistryName()));
	}
}