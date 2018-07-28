package com.nhave.ntechcore.common.block;

import com.nhave.lib.library.client.render.IModelRegister;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineHorizontal extends BlockMachineBase implements IModelRegister
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	public BlockMachineHorizontal(String name)
	{
		super(name);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState)
	{
	    setDefaultFacing(world, blockPos, blockState);
	}
	
	private void setDefaultFacing(World world, BlockPos blockPos, IBlockState blockState)
	{
    	if (world.isRemote)
    	{
    		return;
	    }
	    IBlockState faceNorth = world.getBlockState(blockPos.north());
	    IBlockState faceSouth = world.getBlockState(blockPos.south());
	    IBlockState faceWest = world.getBlockState(blockPos.west());
	    IBlockState faceEast = world.getBlockState(blockPos.east());
	    
	    EnumFacing defaultFace = (EnumFacing)blockState.getValue(FACING);
	    if ((defaultFace == EnumFacing.NORTH) && (faceNorth.isFullBlock()) && (!faceSouth.isFullBlock()))
	    {
	    	defaultFace = EnumFacing.SOUTH;
	    }
	    else if ((defaultFace == EnumFacing.SOUTH) && (faceSouth.isFullBlock()) && (!faceNorth.isFullBlock()))
	    {
	    	defaultFace = EnumFacing.NORTH;
	    }
	    else if ((defaultFace == EnumFacing.WEST) && (faceWest.isFullBlock()) && (!faceEast.isFullBlock()))
	    {
	    	defaultFace = EnumFacing.EAST;
	    }
	    else if ((defaultFace == EnumFacing.EAST) && (faceEast.isFullBlock()) && (!faceWest.isFullBlock()))
	    {
	    	defaultFace = EnumFacing.WEST;
	    }
	    world.setBlockState(blockPos, blockState.withProperty(FACING, defaultFace), 2);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLiving, ItemStack stack)
	{
		super.onBlockPlacedBy(world, blockPos, blockState, entityLiving, stack);
		world.setBlockState(blockPos, blockState.withProperty(FACING, entityLiving.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
	    EnumFacing facing = EnumFacing.getFront(meta);
	    if (facing.getAxis() == EnumFacing.Axis.Y)
	    {
	    	facing = EnumFacing.NORTH;
	    }
	    return getDefaultState().withProperty(FACING, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
	    return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation)
	{
		return state.withProperty(FACING, rotation.rotate((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
	
	public boolean doBlockRotation(World world, BlockPos pos, EnumFacing axis)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet())
        {
            if ((prop.getName().equals("facing") || prop.getName().equals("rotation")) && prop.getValueClass() == EnumFacing.class)
            {
                IBlockState newState;
                IProperty<EnumFacing> facingProperty = (IProperty<EnumFacing>) prop;
                EnumFacing facing = state.getValue(facingProperty);

                newState = state.withProperty(facingProperty, facing.rotateY());

                world.setBlockState(pos, newState);
                return true;
            }
        }
        return false;
    }
	
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModels()
	{
		Item item = Item.getItemFromBlock(this);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getRegistryName().getResourcePath(), "facing=north"));
	}
}