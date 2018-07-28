package com.nhave.ntechcore.common.block;

import java.util.List;

import com.nhave.lib.library.helper.ItemHelper;
import com.nhave.lib.library.helper.TooltipHelper;
import com.nhave.lib.library.helper.WrenchHelper;
import com.nhave.lib.library.util.NumberUtils;
import com.nhave.lib.library.util.StringUtils;
import com.nhave.ntechcore.common.itemupgrade.UpgradeHelper;
import com.nhave.ntechcore.common.tileentity.TileEntityMachine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMachineBase extends BlockBase
{
	private int maxPowerStored = 0;
	
	public BlockMachineBase(String name)
	{
		super(name, Material.IRON);
		this.setHardness(50F);
	}
	
	public BlockMachineBase setMaxStoredPower(int val)
	{
		maxPowerStored = val;
		return this;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		String descUnlocalized = "tooltip.ntechcore.block." + this.getRegistryName().getResourcePath();
		String descLocalized = StringUtils.localize(descUnlocalized);
		boolean info = false;
		
		if (!descLocalized.equals(descUnlocalized))
		{
			if (StringUtils.isShiftKeyDown()) TooltipHelper.addSplitString(tooltip, descLocalized, ";", StringUtils.GRAY);
			else info = true;
		}
		
		int size = tooltip.size();
		addMachineInfo(stack, world, tooltip, flag);
		if (size != tooltip.size()) info = true;
		
		String owner = "";
		boolean isPublic = false;
		
        NBTTagCompound nbttagcompound = stack.getTagCompound();
		if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag"))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");
            
            if (nbttagcompound1 != null && nbttagcompound1.hasKey("OWNER"))
            {
            	owner = nbttagcompound1.getString("OWNER");
            }
            if (nbttagcompound1 != null && nbttagcompound1.hasKey("ISPUBLIC"))
            {
            	isPublic = nbttagcompound1.getBoolean("ISPUBLIC");
            }
        }
		if (!owner.isEmpty())
		{
			if (StringUtils.isShiftKeyDown())
			{
				tooltip.add(StringUtils.localize("tooltip.ntechcore.owner") + ": " + StringUtils.format(owner, StringUtils.YELLOW, StringUtils.ITALIC));
				tooltip.add(StringUtils.localize("tooltip.ntechcore.publicmachine") + ": " + StringUtils.format("" + StringUtils.localize("tooltip.ntechcore.boolean." + isPublic), (isPublic ? StringUtils.GREEN : StringUtils.RED), StringUtils.ITALIC));
			}
			else info = true;
		}
		
		if (info && !StringUtils.isShiftKeyDown()) tooltip.add(StringUtils.shiftForInfo());
		UpgradeHelper.addUpgradeInfo(stack, world, tooltip, flag);
	}

	public void addMachineInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {}
	
	public void addPowerInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) 
	{
		int powerStored = 0;
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag"))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");
            
            if (nbttagcompound1 != null && nbttagcompound1.hasKey("Energy"))
            {
            	powerStored = nbttagcompound1.getInteger("Energy");
            }
        }
		
		if (this.maxPowerStored > 0)
		{
			tooltip.add(StringUtils.localize("tooltip.ntechcore.charge") + ": " + NumberUtils.getDisplayShort(powerStored) + " / " + NumberUtils.getDisplayShort(maxPowerStored) + " " + "RF");
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLiving, ItemStack stack)
	{
		super.onBlockPlacedBy(world, blockPos, blockState, entityLiving, stack);
		
        TileEntity tileentity = (TileEntityMachine) world.getTileEntity(blockPos);
        if (tileentity != null)
        {
            if (tileentity instanceof TileEntityMachine)
            {
            	TileEntityMachine machine = (TileEntityMachine) tileentity;
                machine.speedUpgrade = UpgradeHelper.getMachineSpeed(stack);
                machine.efficincyUpgrade = UpgradeHelper.getMachineEfficiency(stack);
            }
			
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag"))
	        {
	            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");
	            
	            /*if (tileentity instanceof IItemDataTile)
	            {
	            	IItemDataTile tile = (IItemDataTile) tileentity;
	            	tile.readDataFromItemNBT(nbttagcompound1);
	            }*/
	        }
        }
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (!world.isRemote && !player.isCreative())
		{
			ItemStack itemstack = new ItemStack(this);
	        addBlockNBT(itemstack, world, pos, state);
	        ItemHelper.dropItemInWorld(world, pos, itemstack);
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		TileEntity tileentity = world.getTileEntity(pos);
		
        ItemStack itemstack = new ItemStack(this);
        addBlockNBT(itemstack, world, pos, state);
        
        //drops.add(itemstack);
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileEntity tileentity = world.getTileEntity(pos);
		
        ItemStack itemstack = super.getPickBlock(state, target, world, pos, player);
        addBlockNBT(itemstack, world, pos, state);
        
		return itemstack;
	}
	
	public void addBlockNBT(ItemStack stack, IBlockAccess world, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = (TileEntityMachine) world.getTileEntity(pos);
        if (tileentity != null)
        {
        	if (tileentity instanceof TileEntityMachine)
            {
            	TileEntityMachine machine = (TileEntityMachine) tileentity;
    			UpgradeHelper.setMachineUpgrade(stack, "SPEED", machine.speedUpgrade);
    			UpgradeHelper.setMachineUpgrade(stack, "EFFICIENCY", machine.efficincyUpgrade);
            }
			/*if (tileentity instanceof IItemDataTile)
	        {
	        	IItemDataTile tile = (IItemDataTile) tileentity;
	        	
	            NBTTagCompound nbttagcompound = new NBTTagCompound();
	            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	            tile.writeDataToItemNBT(nbttagcompound1);
	            nbttagcompound.setTag("BlockEntityTag", nbttagcompound1);
	            itemstack.setTagCompound(nbttagcompound);
	        }*/
        }
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		return false;
	}
	
	public boolean doBlockRotation(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }
	
	public boolean doBlockActivate(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (hand == EnumHand.MAIN_HAND)
		{
			TileEntity tile = world.getTileEntity(pos);
			/*if (tile instanceof ILockableTile)
			{
				ILockableTile lockable = (ILockableTile) world.getTileEntity(pos);
				if (lockable.hasOwner())
				{
					if (player.isSneaking() && ((lockable.getOwner().equals(player.getName()) && player.getHeldItem(hand).getItem() == ModItems.itemKey) || player.getHeldItem(hand).getItem() == ModItems.itemMasterKey))
					{
						ItemHelper.addItemToPlayer(player, new ItemStack(lockable.isPublic() ? ModItems.itemPublicLock : ModItems.itemLock));
						lockable.setOwner("");
						player.swingArm(EnumHand.MAIN_HAND);
						return !world.isRemote;
					}
					else if (!lockable.isPublic() && !lockable.getOwner().equals(player.getName())) return false;
				}
				else if (player.isSneaking() && (player.getHeldItem(hand).getItem() == ModItems.itemLock || player.getHeldItem(hand).getItem() == ModItems.itemPublicLock))
				{
					lockable.setOwner(player.getName());
					if (player.getHeldItem(hand).getItem() == ModItems.itemPublicLock) lockable.setPublic();
					player.getHeldItem(hand).shrink(1);
					player.swingArm(EnumHand.MAIN_HAND);
					return !world.isRemote;
				}
				
				
			}*/
			
			boolean locked = false;
			/*if (tile instanceof ILockableTile)
			{
				ILockableTile lockable = (ILockableTile) world.getTileEntity(pos);
				if (lockable.hasOwner() && !lockable.getOwner().equals(player.getName()))
				{
					locked = true;
				}
			}*/

			//if (!locked && !player.getHeldItemMainhand().isEmpty() && ItemHelper.isToolWrench(player.getHeldItemMainhand(), pos, player))
			//if (!locked && !player.getHeldItemMainhand().isEmpty() && WrenchHelper.getWrenchUsage(player.getHeldItemMainhand(), player, hand, pos))
			if (!locked && !player.getHeldItemMainhand().isEmpty() && WrenchHelper.canUseWrench(player.getHeldItemMainhand(), player, hand, pos))
			{
				if (player.isSneaking())
				{
					ItemHelper.dismantleBlock(world, pos, state, player);
					WrenchHelper.useWrench(player.getHeldItemMainhand(), player, hand, pos);
					if (!world.isRemote)
					{
						//ItemHelper.useWrench(player.getHeldItemMainhand(), pos, player);
						return true;
					}
					else
					{
						player.playSound(this.blockSoundType.getPlaceSound(), 1.0F, 0.6F);
						player.swingArm(EnumHand.MAIN_HAND);
						return false;
					}
				}
				else if (this.doBlockRotation(world, pos, facing))
				{
					WrenchHelper.useWrench(player.getHeldItemMainhand(), player, hand, pos);
					if (!world.isRemote)
					{
						//ItemHelper.useWrench(player.getHeldItemMainhand(), pos, player);
						return true;
					}
					else
					{
						player.playSound(this.blockSoundType.getPlaceSound(), 1.0F, 0.6F);
						player.swingArm(EnumHand.MAIN_HAND);
						return false;
					}
				}
			}
		}
		return doBlockActivate(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player)
	{
		if (world.getTileEntity(blockPos) != null) world.removeTileEntity(blockPos);
		super.onBlockHarvested(world, blockPos, blockState, player);
	}
}