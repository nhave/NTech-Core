package com.nhave.ntechcore.common.eventhandler;

import com.nhave.ntechcore.common.item.ItemRunningBoots;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorEventHandler
{
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event)
	{
		EntityLivingBase base = event.getEntityLiving();
		ItemStack stack = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		
		if(!stack.isEmpty() && stack.getItem() instanceof ItemRunningBoots)
		{
			ItemRunningBoots boots = (ItemRunningBoots) stack.getItem();
			
			if (event.getSource() == DamageSource.FALL)
			{
				if (event.getAmount() <= 3)
				{
					event.setCanceled(true);
				}
			}
		}
	}
}