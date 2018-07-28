package com.nhave.ntechcore.common.block;

import com.nhave.lib.library.helper.ItemHelper;
import com.nhave.lib.library.tile.ITileRegister;
import com.nhave.ntechcore.client.render.tileentity.RenderTileIO;
import com.nhave.ntechcore.common.tileentity.TileEntityIO;
import com.nhave.ntechcore.common.tileentity.TileEntityMachine;
import com.nhave.ntechcore.core.NTechCore;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineIO extends BlockMachineBase implements ITileRegister
{
    public static final PropertyEnum<BlockMachineIO.EnumBlockIOMode> IOMODE = PropertyEnum.<BlockMachineIO.EnumBlockIOMode>create("iomode", BlockMachineIO.EnumBlockIOMode.class);
	
	public BlockMachineIO(String name)
	{
		super(name);
	}
	
	@Override
	public boolean doBlockRotation(World world, BlockPos pos, EnumFacing axis)
	{
		if (axis != EnumFacing.UP)
		{
			IBlockState state = world.getBlockState(pos);
			world.setBlockState(pos, state.cycleProperty(BlockMachineIO.IOMODE));
			world.markChunkDirty(pos, null);
			return true;
		}
		return false;
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
    			TileEntityIO tile = (TileEntityIO) world.getTileEntity(blockPos);
    			
    			ItemStack[] items = tile.getItemStacks();
    			for (int i = 0; i < items.length; ++i)
    			{
    				if (!items[i].isEmpty())
    				{
    					ItemHelper.dropItemInWorld(world, blockPos, items[i]);
    				}
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
		return new TileEntityIO();
	}
	
	@Override
	public void registerTileEntity()
	{
		NTechCore.logger.info("Registering TileEntity \"TileEntityIO\" to block: " + this.getRegistryName());
		GameRegistry.registerTileEntity(com.nhave.ntechcore.common.tileentity.TileEntityIO.class, new ResourceLocation(Reference.MODID, "TileIO"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean registerTileEntityRender()
	{
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIO.class, new RenderTileIO());
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
	public IBlockState getStateFromMeta(int meta)
	{
	    return getDefaultState().withProperty(IOMODE, EnumBlockIOMode.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
	    return state.getValue(IOMODE).ordinal();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { IOMODE });
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(IOMODE, BlockMachineIO.EnumBlockIOMode.INPUT);
    }
	
	public static enum EnumBlockIOMode implements IStringSerializable
    {
        INPUT("input"),
        OUTPUT("output");
		
        private final String name;
        
        private EnumBlockIOMode(String name)
        {
            this.name = name;
        }
        
        public String toString()
        {
            return this.name;
        }
        
        public String getName()
        {
            return this.name;
        }
    }
}