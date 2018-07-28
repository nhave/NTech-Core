package com.nhave.ntechcore.common.content;

import java.util.ArrayList;
import java.util.List;

import com.nhave.lib.library.client.render.IModelRegister;
import com.nhave.lib.library.helper.ItemNBTHelper;
import com.nhave.lib.library.item.IOreRegister;
import com.nhave.lib.library.util.ColorUtils;
import com.nhave.ntechcore.common.armorplate.ArmorPlate;
import com.nhave.ntechcore.common.chroma.Chroma;
import com.nhave.ntechcore.common.item.ItemArmorPlate;
import com.nhave.ntechcore.common.item.ItemChroma;
import com.nhave.ntechcore.common.item.ItemJumpUpgrade;
import com.nhave.ntechcore.common.item.ItemMeta;
import com.nhave.ntechcore.common.item.ItemOredict;
import com.nhave.ntechcore.common.item.ItemPlatedArmor;
import com.nhave.ntechcore.common.item.ItemRunningBoots;
import com.nhave.ntechcore.common.item.ItemShader;
import com.nhave.ntechcore.common.item.ItemSimpleUpgrades;
import com.nhave.ntechcore.common.item.ItemWrench;
import com.nhave.ntechcore.common.itemupgrade.UpgradeManager;
import com.nhave.ntechcore.core.NTechCore;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems
{
	public static final ModItems INSTANCE = new ModItems();
	
	private static final List<Item> ITEMS = new ArrayList<Item>();

	public static final ArmorMaterial MATERIAL_NOARMOR = EnumHelper.addArmorMaterial("NH:NOARMOR", "zero_armor", 0, new int[] {0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	public static final ArmorMaterial MATERIAL_CARBON = EnumHelper.addArmorMaterial("NH:CARBON", "carbon_armor", 0, new int[] {3, 6, 8, 3}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	public static final ArmorMaterial MATERIAL_STEEL = EnumHelper.addArmorMaterial("NH:STEEL", "steel_armor", 22, new int[] {2, 5, 7, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1F);
	
	public static final String[][] CIRCUITS = new String[][]
	{
		{"basic", "1"}, {"advanced", "2"}, {"elite", "3"}, {"ultimate", "4"}
	};
	public static final String[][] INGOTS = new String[][]
	{
		{"steel", "2"}
	};
	public static final String[][] PLATES = new String[][]
	{
		{"iron", "0"}, {"gold", "0"}, {"steel", "2"}, {"carbon", "2"}, {"plastic", "1"}, {"glassfiber", "1"}
	};
	public static final String[][] JUMP_KITS = new String[][]
	{
		{"double", "2"}, {"triple", "2"}, {"velocity", "2"}
	};
	public static final String[][] MACHINE_UPGRADES = new String[][]
	{
		{"speed1", "2", "SPEED"}, {"speed2", "3", "SPEED"}, {"efficiency1", "2", "EFFICIENCY"}, {"efficiency2", "3", "EFFICIENCY"}, {"automation", "2", "AUTOMATION"}
	};
	public static final String[][] MACHINE_SPECIALS = new String[][]
	{
		{"uninstall", "3", "UNINSTALL"}
	};
	public static final String[] MODELS = new String[]
	{
		"upgradeslider", "upgrademover", "upgradelaser"
	};
	
	public static Item itemWrench;
	public static Item itemCircuits;
	public static Item itemPlates;
	public static Item itemIngots;
	public static Item itemPlateHelmet;
	public static Item itemPlateChest;
	public static Item itemPlateLegs;
	public static Item itemPlateBoots;
	public static Item itemArmorplates;
	public static Item itemRunningBoots;
	public static Item itemUpgradeJump;
	public static Item itemChroma;
	public static Item itemShader;
	public static Item itemMachineUpgrades;
	public static Item itemMachineSpecials;
	public static Item itemModels;
	
	public static ArmorPlate plateIron;
	public static ArmorPlate plateGold;
	public static ArmorPlate plateDiamond;
	public static ArmorPlate plateSteel;
	public static ArmorPlate plateCarbon;
	
	public static Chroma[] chromaBasic = new Chroma[16];
	
	public static void init()
	{
		itemWrench = new ItemWrench("wrench");
		itemCircuits = new ItemOredict("circuit", "circuit", CIRCUITS);
		itemIngots = new ItemOredict("ingot", "ingot", INGOTS);
		itemPlates = new ItemOredict("plate", "plate", PLATES);
		itemPlateHelmet = new ItemPlatedArmor("platehelmet", MATERIAL_NOARMOR, EntityEquipmentSlot.HEAD);
		itemPlateChest = new ItemPlatedArmor("platechest", MATERIAL_NOARMOR, EntityEquipmentSlot.CHEST);
		itemPlateLegs = new ItemPlatedArmor("platelegs", MATERIAL_NOARMOR, EntityEquipmentSlot.LEGS);
		itemPlateBoots = new ItemPlatedArmor("plateboots", MATERIAL_NOARMOR, EntityEquipmentSlot.FEET);
		itemArmorplates = new ItemArmorPlate("armorplate").setCreativeTab(ModCreativeTabs.getUpgrades());
		itemRunningBoots = new ItemRunningBoots("runningboots", MATERIAL_NOARMOR);
		itemUpgradeJump = new ItemJumpUpgrade("jumpkit", JUMP_KITS).setCreativeTab(ModCreativeTabs.getUpgrades());
		itemChroma = new ItemChroma("chroma").setCreativeTab(ModCreativeTabs.getUpgrades());
		itemShader = new ItemShader("shader").setCreativeTab(ModCreativeTabs.getUpgrades());
		itemMachineUpgrades = new ItemSimpleUpgrades("machineupgrade", MACHINE_UPGRADES).setCreativeTab(ModCreativeTabs.getUpgrades());
		itemMachineSpecials = new ItemSimpleUpgrades("machinespecial", MACHINE_SPECIALS).setCreativeTab(ModCreativeTabs.getUpgrades());
		itemModels = new ItemMeta("modelref", MODELS, 0).setCreativeTab(null);
		
		plateIron = new ArmorPlate("iron", ArmorMaterial.IRON, 0);
		plateGold = new ArmorPlate("gold", ArmorMaterial.GOLD, 0);
		plateDiamond = new ArmorPlate("diamond", ArmorMaterial.DIAMOND, 2);
		plateSteel = new ArmorPlate("steel", MATERIAL_STEEL, 2);
		plateCarbon = new ArmorPlate("carbon", MATERIAL_CARBON, 2);
		
		for (int i = 0; i < chromaBasic.length; ++i)
		{
			chromaBasic[i] = new Chroma(ColorUtils.colorNames[i], ColorUtils.colorCodes[i]);
		}
		
		UpgradeManager.addUpgrade(itemRunningBoots, new ItemStack(itemUpgradeJump));
		
		ModCreativeTabs.getItems().setTabIconItem(new ItemStack(itemWrench));
		ModCreativeTabs.getArmor().setTabIconItem(new ItemStack(itemRunningBoots));
		ModCreativeTabs.getUpgrades().setTabIconItem(new ItemStack(itemUpgradeJump));
		
		addDefaultMachineUpgrades(Item.getItemFromBlock(ModBlocks.blockUpgrade));
		UpgradeManager.addUpgrade(Item.getItemFromBlock(ModBlocks.blockUpgrade), new ItemStack(itemMachineSpecials, 1, 0));
		UpgradeManager.addUpgrade(Item.getItemFromBlock(ModBlocks.blockUpgrade), new ItemStack(itemMachineUpgrades, 1, 4));
		addDefaultMachineUpgrades(Item.getItemFromBlock(ModBlocks.blockFabricator));
	}
	
	public static void addDefaultMachineUpgrades(Item item)
	{
		UpgradeManager.addUpgrade(item, new ItemStack(itemMachineUpgrades, 1, 0));
		UpgradeManager.addUpgrade(item, new ItemStack(itemMachineUpgrades, 1, 1));
		UpgradeManager.addUpgrade(item, new ItemStack(itemMachineUpgrades, 1, 2));
		UpgradeManager.addUpgrade(item, new ItemStack(itemMachineUpgrades, 1, 3));
	}
	
	public static Item addItem(Item item)
	{
		ITEMS.add(item);
		return item;
	}
	
	public static List<Item> getItems()
	{
		return ITEMS;
	}
	
	public static void register(Register<Item> event)
	{
		for (Item item : getItems())
		{
			NTechCore.logger.info("Registering Item: " + item.getRegistryName());
			event.getRegistry().register(item);
			if (item instanceof IOreRegister) ((IOreRegister) item).registerOres();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		for (Item item : getItems())
		{
			NTechCore.logger.info("Registering Renderers for Item: " + item.getRegistryName());
			if (item instanceof IModelRegister) ((IModelRegister) item).registerModels();
			else registerRender(item);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerRender(Item item)
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getRegistryName().getResourcePath(), "inventory"));
	}
	
	public static ItemStack createItemStack(Item item, String name, int amount)
	{
		if (item instanceof ItemMeta)
		{
			ItemMeta metaItem = (ItemMeta) item;
			for (int meta = 0; meta < metaItem.names.length; ++meta)
			{
				if (metaItem.names[meta].toLowerCase().equals(name.toLowerCase())) return new ItemStack(item, amount, meta);
			}
		}
		else if (item == itemArmorplates)
		{
			return ItemNBTHelper.setString(new ItemStack(item, amount), "ARMORPLATE", "ARMORPLATE", name);
		}
		return new ItemStack(item, amount);
	}
	
	public static ItemStack createItemStack(Item item, String name)
	{
		return createItemStack(item, name, 1);
	}
	
	public static ItemStack createItemStack(ArmorPlate plate, int amount)
	{
		return createItemStack(itemArmorplates, plate.getName(), amount);
	}
	
	public static ItemStack createItemStack(ArmorPlate plate)
	{
		return createItemStack(itemArmorplates, plate.getName(), 1);
	}
}