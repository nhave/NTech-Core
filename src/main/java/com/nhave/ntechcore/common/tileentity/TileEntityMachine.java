package com.nhave.ntechcore.common.tileentity;

import com.nhave.ntechcore.common.block.BlockMachineIO;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMachine extends TileEntity
{
	public int speedUpgrade = 0;
	public int efficincyUpgrade = 0;
	private String owner = "";
	private boolean isPublic = false;
	
	public boolean onTileActivated(World world, EntityPlayer player)
	{
		return false;
	}
	
	public boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
    {
		if (stackA.isEmpty() && stackB.isEmpty())
        {
            return true;
        }
        else if (stackA.getItem() != stackB.getItem())
        {
            return false;
        }
        else if (stackA.getItemDamage() != stackB.getItemDamage())
        {
            return false;
        }
        else if (stackA.getTagCompound() == null && stackB.getTagCompound() != null)
        {
            return false;
        }
        else
        {
            return (stackA.getTagCompound() == null || stackA.getTagCompound().equals(stackB.getTagCompound())) && stackA.areCapsCompatible(stackB);
        }
    }
	
	public TileEntityIO getNearbyInput()
	{
		for (int i = 0; i < EnumFacing.values().length; ++i)
		{
			TileEntity tile = getWorld().getTileEntity(getPos().offset(EnumFacing.values()[i]));
			if (tile != null && tile instanceof TileEntityIO)
			{
				if (((TileEntityIO) tile).getIOMode() == BlockMachineIO.EnumBlockIOMode.INPUT) return (TileEntityIO) tile;
			}
		}
		return null;
	}
	
	public TileEntityIO getNearbyOutput()
	{
		for (int i = 0; i < EnumFacing.values().length; ++i)
		{
			TileEntity tile = getWorld().getTileEntity(getPos().offset(EnumFacing.values()[i]));
			if (tile != null && tile instanceof TileEntityIO)
			{
				if (((TileEntityIO) tile).getIOMode() == BlockMachineIO.EnumBlockIOMode.OUTPUT) return (TileEntityIO) tile;
			}
		}
		return null;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return false;
	}
	
	protected void sync()
	{
		world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		markDirty();
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		writeSyncableDataToNBT(tag);
		return tag;
	}
	
	public void writeSyncableDataToNBT(NBTTagCompound tag)
	{
		//writeDataToItemNBT(tag);
		tag.setInteger("SPEEDUPGRADE", this.speedUpgrade);
		tag.setInteger("EFFICIENCYUPGRADE", this.efficincyUpgrade);
	}
	
	public void writeDataToItemNBT(NBTTagCompound tag)
	{
		tag.setString("OWNER", this.owner);
		tag.setBoolean("ISPUBLIC", this.isPublic);
	}
	
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		readSyncableDataFromNBT(tag);
	}
	
	public void readSyncableDataFromNBT(NBTTagCompound tag)
	{
		//readDataFromItemNBT(tag);
		this.speedUpgrade = tag.getInteger("SPEEDUPGRADE");
		this.efficincyUpgrade = tag.getInteger("EFFICIENCYUPGRADE");
	}
	
	public void readDataFromItemNBT(NBTTagCompound tag)
	{
		this.owner = tag.getString("OWNER");
		this.isPublic = tag.getBoolean("ISPUBLIC");
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound p_handleUpdateTag_1_)
	{
		readSyncableDataFromNBT(p_handleUpdateTag_1_);
		super.handleUpdateTag(p_handleUpdateTag_1_);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncableDataToNBT(syncData);
		return new SPacketUpdateTileEntity(this.pos, 3, syncData);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}
}