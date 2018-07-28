package com.nhave.ntechcore.common.tileentity;

import com.nhave.lib.library.helper.ItemHelper;
import com.nhave.ntechcore.common.block.BlockMachineIO;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class TileEntityIO extends TileEntityMachine
{
	private ItemStack[] items;
	
	public TileEntityIO()
	{
		items = new ItemStack[9];
		for (int i = 0; i < items.length; ++i)
		{
			items[i] = ItemStack.EMPTY;
		}
	}
	
	@Override
	public boolean onTileActivated(World world, EntityPlayer player)
	{
		if (!player.getHeldItemMainhand().isEmpty())
		{
			for (int i = 0; i < this.items.length; ++i)
			{
				if (areItemStacksEqual(this.items[i], player.getHeldItemMainhand()))
				{
					int size = this.items[i].getCount();
					int needed = this.items[i].getMaxStackSize() - size;
					
					if (needed > 0)
					{
						int held = player.getHeldItemMainhand().getCount();
						this.items[i].grow(Math.min(needed, held));
						player.getHeldItemMainhand().shrink(Math.min(needed, held));
						
						sync();
						player.swingArm(EnumHand.MAIN_HAND);
						return true;
					}
				}
			}
			
			for (int i = 0; i < this.items.length; ++i)
			{
				if (this.items[i].isEmpty())
				{
					this.items[i] = player.getHeldItemMainhand().copy();
					player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
					
					sync();
					player.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
			}
		}
		else
		{
			for (int i = (this.items.length - 1); i >= 0; --i)
			{
				if (!this.items[i].isEmpty())
				{
					if (!world.isRemote && player.getHeldItemMainhand().isEmpty()) player.setHeldItem(EnumHand.MAIN_HAND, items[i].copy());
					else ItemHelper.addItemToPlayer(player, items[i].copy());
					this.items[i] = ItemStack.EMPTY;
					
					sync();
					player.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ItemStack[] getItemStacks()
	{
		return this.items;
	}
	
	public boolean canFitItemStack(ItemStack stack)
	{
		int stackSize = stack.getCount();
		
		for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i].isEmpty())
			{
				return true;
			}
			else if (areItemStacksEqual(this.items[i], stack))
			{
				int size = this.items[i].getCount();
				int needed = this.items[i].getMaxStackSize() - size;
				
				if (needed > 0) stackSize -= needed;
			}
			if (stackSize <= 0) return true;
		}
		return false;
		
		/*for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i].isEmpty())
			{
				return true;
			}
			else if (areItemStacksEqual(this.items[i], stack))
			{
				int size = this.items[i].getCount();
				int needed = this.items[i].getMaxStackSize() - size;
				
				if (needed > 0 && needed <= stack.getCount())
				{
					return true;
				}
			}
		}
		return false;*/
	}
	
	public boolean addItemStack(ItemStack stack)
	{
		int stackSize = stack.getCount();
		
		for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i].isEmpty())
			{
				this.items[i] = stack.copy();
				sync();
				return true;
			}
			else if (areItemStacksEqual(this.items[i], stack))
			{
				int size = this.items[i].getCount();
				int needed = this.items[i].getMaxStackSize() - size;
				
				if (needed > 0)
				{
					this.items[i].grow(Math.min(needed, stackSize));
					sync();
					stackSize -= needed;
				}
			}
			if (stackSize <= 0) return true;
		}
		return false;
		
		/*int held = stack.getCount();
		for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i].isEmpty())
			{
				this.items[i] = stack.copy();
				sync();
				return true;
			}
			else if (areItemStacksEqual(this.items[i], stack))
			{
				int size = this.items[i].getCount();
				int needed = this.items[i].getMaxStackSize() - size;
				
				if (needed > 0)
				{
					this.items[i].grow(Math.min(needed, held));
					//player.getHeldItemMainhand().shrink(Math.min(needed, held));
					
					sync();
					//player.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
			}
		}
		return false;*/
	}
	
	public boolean isInventoryEmpty()
	{
		for (int i = 0; i < this.items.length; ++i)
		{
			if (!this.items[i].isEmpty())
			{
				return false;
			}
		}
		return true;
	}
	
	public BlockMachineIO.EnumBlockIOMode getIOMode()
	{
		IBlockState state = this.getWorld().getBlockState(this.getPos());
		return BlockMachineIO.EnumBlockIOMode.values() [state.getBlock().getMetaFromState(state)];
	}

	@Override
	public void writeSyncableDataToNBT(NBTTagCompound tag)
	{
		super.writeSyncableDataToNBT(tag);
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < this.items.length; ++i)
		{
			if(!this.items[i].isEmpty())
			{
				NBTTagCompound tag1 = new NBTTagCompound();
				
				tag1.setByte("Slot", (byte)i);
				this.items[i].writeToNBT(tag1);
				
				tagList.appendTag(tag1);
			}
		}
		tag.setTag("ITEM", tagList);
	}
	
	public void readSyncableDataFromNBT(NBTTagCompound tag)
	{
		super.readSyncableDataFromNBT(tag);
		NBTTagList items = tag.getTagList("ITEM", tag.getId());
		
		for (int i = 0; i < this.items.length; ++i)
		{
			NBTTagCompound item = items.getCompoundTagAt(i);
	        this.items[i] = new ItemStack(item).copy();
		}
	}
}