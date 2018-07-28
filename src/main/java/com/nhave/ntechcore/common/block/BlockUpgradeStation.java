package com.nhave.ntechcore.common.block;

import java.util.List;

import com.nhave.lib.library.helper.ItemHelper;
import com.nhave.lib.library.tile.ITileRegister;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.client.render.tileentity.RenderTileUpgradeStation;
import com.nhave.ntechcore.common.content.ModItems;
import com.nhave.ntechcore.common.itemupgrade.UpgradeHelper;
import com.nhave.ntechcore.common.tileentity.TileEntityMachine;
import com.nhave.ntechcore.common.tileentity.TileEntityUpgradeStation;
import com.nhave.ntechcore.core.NTechCore;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUpgradeStation extends BlockMachineHorizontal implements ITileRegister
{
	public BlockUpgradeStation(String name)
	{
		super(name);
	}
	
	@Override
	public void addMachineInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		if (StringUtils.isShiftKeyDown())
		{
			addPowerInfo(stack, world, tooltip, flag);

			int speedUpgrade = UpgradeHelper.getMachineSpeed(stack);
			int efficincy = UpgradeHelper.getMachineEfficiency(stack)+1;
			int speed = (UpgradeHelper.getMachineSpeed(stack)*2)+speedUpgrade;
			int powercost = (10 * (speed > 0 ? speed : 1))/efficincy;
			tooltip.add(StringUtils.format(powercost + " " + "RF" + " " + StringUtils.localize("tooltip.ntechcore.charge.tick"), StringUtils.ORANGE));
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLiving, ItemStack stack)
	{
		super.onBlockPlacedBy(world, blockPos, blockState, entityLiving, stack);
		
		TileEntityUpgradeStation tileentity = (TileEntityUpgradeStation) world.getTileEntity(blockPos);

		ItemStack upgradeUninstall = ItemUtil.getItemFromStack(stack, "UNINSTALL");
		if (upgradeUninstall != null && upgradeUninstall.getItem() == ModItems.itemMachineSpecials && upgradeUninstall.getItemDamage() == 0)
		{
			tileentity.canUninstall = true;
		}
		ItemStack upgradeAutomation = ItemUtil.getItemFromStack(stack, "AUTOMATION");
		if (upgradeAutomation != null && upgradeAutomation.getItem() == ModItems.itemMachineUpgrades && upgradeAutomation.getItemDamage() == 4)
		{
			tileentity.canUseIO = true;
		}
	}
	
	public void addBlockNBT(ItemStack stack, IBlockAccess world, BlockPos pos, IBlockState state)
	{
		super.addBlockNBT(stack, world, pos, state);
		
		TileEntityUpgradeStation tileentity = (TileEntityUpgradeStation) world.getTileEntity(pos);
		if (tileentity != null)
		{
			if (tileentity.canUninstall)
			{
				ItemUtil.addItemToStack(stack, new ItemStack(ModItems.itemMachineSpecials, 1, 0), "UNINSTALL");
			}
			if (tileentity.canUseIO)
			{
				ItemUtil.addItemToStack(stack, new ItemStack(ModItems.itemMachineUpgrades, 1, 4), "AUTOMATION");
			}
		}
	}
	
	@Override
	public boolean doBlockRotation(World world, BlockPos pos, EnumFacing axis)
	{
		if (axis == EnumFacing.UP) return false;
		return super.doBlockRotation(world, pos, axis);
	}
	
	@Override
	public boolean doBlockActivate(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntityMachine tile = (TileEntityMachine) worldIn.getTileEntity(pos);
		if (tile != null && hand == EnumHand.MAIN_HAND)
		{
			return tile.onTileActivated(worldIn, playerIn);
		}
		return false;
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player)
	{
		if (!world.isRemote)
        {
    		if (world.getTileEntity(blockPos) != null)
    		{
    			TileEntityUpgradeStation tile = (TileEntityUpgradeStation) world.getTileEntity(blockPos);
    			
    			ItemStack[] items = tile.getItemStacks();
    			
				if (!items[0].isEmpty())
				{
					ItemHelper.dropItemInWorld(world, blockPos, items[0]);
				}
				if (tile.dropUpgrade && !items[1].isEmpty())
				{
					ItemHelper.dropItemInWorld(world, blockPos, items[1]);
				}
    		}
        }
		super.onBlockHarvested(world, blockPos, blockState, player);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityUpgradeStation();
	}
	
	@Override
	public void registerTileEntity()
	{
		NTechCore.logger.info("Registering TileEntity \"TileEntityUpgradeStation\" to block: " + this.getRegistryName());
		GameRegistry.registerTileEntity(com.nhave.ntechcore.common.tileentity.TileEntityUpgradeStation.class, new ResourceLocation(Reference.MODID, "TileUpgrade"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean registerTileEntityRender()
	{
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityUpgradeStation.class, new RenderTileUpgradeStation());
        return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModels()
	{
		Item item = Item.getItemFromBlock(this);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getRegistryName().getResourcePath(), "inventory"));
	}
}