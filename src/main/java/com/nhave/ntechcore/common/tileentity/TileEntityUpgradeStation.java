package com.nhave.ntechcore.common.tileentity;

import java.util.Random;

import com.nhave.lib.library.helper.ItemHelper;
import com.nhave.lib.library.util.ItemUtil;
import com.nhave.ntechcore.common.event.ItemUpgradeEvent;
import com.nhave.ntechcore.common.itemupgrade.UpgradeManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityUpgradeStation extends TileEntityMachine implements ITickable
{
	public boolean canUninstall = false;
	public boolean canUseIO = false;
	
	private ItemStack[] items;
	private int cycles = 0;
	private int timeLeft = 0;
	private boolean uninstall = false;
	public boolean dropUpgrade = false;

	public float sliderX = 0.5F;
	public float sliderZ = 0.5F;
	private int timeAtDestination = 0;
	private float destinationX = 0.5F;
	private float destinationZ = 0.5F;
	public boolean laserOn = false;
	
	public TileEntityUpgradeStation()
	{
		items = new ItemStack[3];
		for (int i = 0; i < items.length; ++i)
		{
			items[i] = ItemStack.EMPTY;
		}
	}
	
	@Override
	public void update()
	{
		if (this.cycles > 0)
		{
			if (this.canUseIO)
			{
				if (!this.uninstall && this.items[2].isEmpty() && getNearbyInput() != null)
				{
					TileEntityIO io = getNearbyInput();
					attemptCrafting(this.items[0], io.getItemStacks()[Math.max(0, Math.min(io.getItemStacks().length-1, 9-this.cycles))]);
				}
				
				if (this.uninstall && getNearbyOutput() == null)
				{
					this.cycles = 0;
					this.timeLeft = 0;
					this.uninstall = false;
				}
				else if (this.uninstall && this.timeLeft == 0 && !this.items[0].isEmpty())
				{
					TileEntityIO io = getNearbyOutput();
					if (UpgradeManager.hasUpgrades(this.items[0].getItem()))
					{
						NonNullList<String> list = UpgradeManager.getNBTList(this.items[0].getItem());
						String nbt = list.get(Math.max(0, Math.min(list.size()-1, cycles-1)));
						ItemStack upgrade = ItemUtil.getItemFromStack(this.items[0], nbt);
						if (upgrade != null && !upgrade.isEmpty() && io.canFitItemStack(upgrade))
						{
							io.addItemStack(upgrade);
							ItemUtil.removeAllItemFromStack(this.items[0], nbt);
							
							if (cycles > 1)
							{
								this.timeLeft = 100;
								String nbtA = list.get(Math.max(0, Math.min(list.size()-1, cycles-2)));
								ItemStack upgradeA = ItemUtil.getItemFromStack(this.items[0], nbtA);
								if (upgradeA != null && !upgradeA.isEmpty() && io.canFitItemStack(upgradeA))
								{
									this.timeLeft = 100;
								}
							}
							sync();
						}
					}
				}
			}
			
			if (this.timeLeft > 0)
			{
				int speed = this.speedUpgrade*2;
				this.timeLeft -= (speed > 0 ? speed : 1);
			}
			else
			{
				if (!this.items[2].isEmpty())
				{
					this.items[0] = this.items[2].copy();
					this.items[1] = ItemStack.EMPTY;
					this.items[2] = ItemStack.EMPTY;
					dropUpgrade = false;
					sync();
				}
				if (this.timeLeft <= 0) this.cycles--;
			}
		}
		else
		{
			this.cycles = 0;
			this.timeLeft = 0;
			this.uninstall = false;
		}
		
		if (getWorld().isRemote)
		{
			updateClient();
		}
	}
	
	public void updateClient()
	{
		int speed = this.speedUpgrade*2;
		for (int i = 0; i < (speed > 0 ? speed : 1); ++i)
		{
			boolean xMoving = true;
			boolean zMoving = true;
			if (sliderX < (destinationX - 0.01F) || sliderX > (destinationX + 0.01F)) sliderX += (destinationX > sliderX ? 0.02F : -0.02F);
			else xMoving = false;
			if (sliderZ < (destinationZ - 0.01F) || sliderZ > (destinationZ + 0.01F)) sliderZ += (destinationZ > sliderZ ? 0.02F : -0.02F);
			else zMoving = false;
			
			if (this.cycles > 0)
			{
				if (!xMoving && !zMoving && this.timeLeft > 0)
				{
					timeAtDestination++;
				}
				else timeAtDestination = 0;
				
				if ((timeAtDestination > 4 && timeAtDestination < 8) || (timeAtDestination > 10 && timeAtDestination < 15)) laserOn = true;
				else laserOn = false;
				
				if (timeAtDestination >= 20)
				{
					timeAtDestination = 0;
					Random rand = new Random();
					destinationX = (((float)rand.nextInt(6)) / 10) + 0.5F;
					destinationZ = (((float)rand.nextInt(6)) / 10) + 0.5F;
				}
			}
			else
			{
				laserOn = false;
				timeAtDestination = 0;
				destinationX = 0.5F;
				destinationZ = 0.5F;
			}
		}
	}
	
	private boolean attemptCrafting(ItemStack stackA, ItemStack stackB)
	{
		ItemUpgradeEvent evt = new ItemUpgradeEvent(stackA, stackB);
		MinecraftForge.EVENT_BUS.post(evt);
		
		if (!evt.isCanceled() && evt.getOutput() != null && evt.getMaterialCost() <= stackB.getCount())
		{
			this.items[1] = stackB.copy();
			this.items[1].setCount(1);
			this.items[2] = evt.getOutput().copy();
			
			if (evt.getMaterialCost() > 0)
			{
				stackB.shrink(evt.getMaterialCost());
				this.items[1].setCount(evt.getMaterialCost());
				dropUpgrade = true;
			}
			this.timeLeft = evt.getCraftingTime();
			
			sync();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onTileActivated(World world, EntityPlayer player)
	{
		if (this.cycles > 0) return false;
		if (this.items[0].isEmpty() && !player.getHeldItemMainhand().isEmpty())
		{
			this.items[0] = player.getHeldItemMainhand().copy();
			this.items[0].setCount(1);
			player.getHeldItemMainhand().shrink(1);
			
			sync();
			player.swingArm(EnumHand.MAIN_HAND);
			return true;
		}
		else if (!this.items[0].isEmpty())
		{
			if (!player.isSneaking())
			{
				if (attemptCrafting(this.items[0], player.getHeldItemMainhand()))
				{
					startOperation(1);
					
					sync();
					player.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
				else
				{
					if (!world.isRemote && player.getHeldItemMainhand().isEmpty()) player.setHeldItem(EnumHand.MAIN_HAND, items[0].copy());
					else ItemHelper.addItemToPlayer(player, items[0].copy());
					this.items[0] = ItemStack.EMPTY;
					
					sync();
					player.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
			}
			else if (this.canUseIO && player.isSneaking() && player.getHeldItemMainhand().isEmpty())
			{
				if (getNearbyInput() != null && !getNearbyInput().isInventoryEmpty())
				{
					TileEntityIO io = getNearbyInput();
					for (int i = 0; i < io.getItemStacks().length; ++i)
					{
						if (attemptCrafting(this.items[0], io.getItemStacks()[Math.max(0, Math.min(io.getItemStacks().length-1, i))]))
						{
							startOperation(9);
							
							sync();
							player.swingArm(EnumHand.MAIN_HAND);
							return true;
						}
					}
				}
				else if (canUninstall && getNearbyOutput() != null)
				{
					if (UpgradeManager.hasUpgrades(this.items[0].getItem()))
					{
						for (String nbt : UpgradeManager.getNBTList(this.items[0].getItem()))
						{
							ItemStack upgrade = ItemUtil.getItemFromStack(this.items[0], nbt);
							if (upgrade != null && !upgrade.isEmpty() && getNearbyOutput().canFitItemStack(upgrade)) 
							{
								startOperation(UpgradeManager.getNBTList(this.items[0].getItem()).size());
								this.timeLeft = 100;
								break;
							}
						}
						
						if (this.cycles > 0)
						{
							this.uninstall = true;
							player.swingArm(EnumHand.MAIN_HAND);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private void startOperation(int cycles)
	{
		this.cycles = cycles;
		if (cycles > 0 && this.getWorld().isRemote)
		{
			Random rand = new Random();
			destinationX = (((float)rand.nextInt(6)) / 10) + 0.5F;
			destinationZ = (((float)rand.nextInt(6)) / 10) + 0.5F;
		}
	}
	
	public ItemStack[] getItemStacks()
	{
		return this.items;
	}
	
	public boolean isRunning()
	{
		return this.cycles > 0;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		tag.setBoolean("UNINSTALLUPGRADE", this.canUninstall);
		tag.setBoolean("AUTOMATIONUPGRADE", this.canUseIO);
		
		return tag;
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
		
		tag.setInteger("CYCLES", this.cycles);
		tag.setInteger("TIME", this.timeLeft);
		tag.setBoolean("UNINSTALL", this.uninstall);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		this.canUninstall = tag.getBoolean("UNINSTALLUPGRADE");
		this.canUseIO = tag.getBoolean("AUTOMATIONUPGRADE");
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
		
		this.cycles = tag.getInteger("CYCLES");
		this.timeLeft = tag.getInteger("TIME");
		this.uninstall = tag.getBoolean("UNINSTALL");
	}
}