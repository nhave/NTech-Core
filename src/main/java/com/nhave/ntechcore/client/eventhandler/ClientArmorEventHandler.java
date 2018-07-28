package com.nhave.ntechcore.client.eventhandler;

import com.nhave.ntechcore.api.item.armor.IMultiJump;
import com.nhave.ntechcore.api.item.armor.IStepAssist;
import com.nhave.ntechcore.common.network.MessageOnDoubleJump;
import com.nhave.ntechcore.common.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientArmorEventHandler
{
	private boolean hasIncreasedStep = false;
	private boolean jumpPressed = false;
	private int jumpCount = 0;
	
	@SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent evt)
	{
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
        	if (evt.phase == TickEvent.Phase.END)
        	{
	        	EntityPlayer player = (EntityPlayer) evt.player;
	            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
	            
	            if (!player.isSneaking() && boots.getItem() instanceof IStepAssist && ((IStepAssist) boots.getItem()).getStepHeight(boots) > 0.6F)
	            {
	            	player.stepHeight = 1.25F;
	            	hasIncreasedStep = true;
	            }
	            else if (hasIncreasedStep)
	            {
	            	player.stepHeight = 0.6F;
	            	hasIncreasedStep = false;
	            }
	            
	            if (boots.getItem() instanceof IMultiJump && ((IMultiJump) boots.getItem()).getInAirJumps(boots) > 0 && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
	            {
	            	int jumpsAvailable = ((IMultiJump) boots.getItem()).getInAirJumps(boots);
	            	float jumpHeight = ((IMultiJump) boots.getItem()).getJumpHeight(boots);
	            	float jumpVelocity = ((IMultiJump) boots.getItem()).getJumpVelocity(boots);
	            	
	            	if (!jumpPressed)
	            	{
	                	jumpPressed = true;
	                	
	                	boolean canJump = (!player.isSpectator() && !player.isCreative() && !player.isInWater() && !player.isOnLadder() && !player.isInLava());
	                	
	                	if (canJump && jumpCount < jumpsAvailable)
	                    {
	                    	player.motionY = jumpHeight;
	                    	//player.fallDistance = 0;
	                    	
	                    	if (jumpVelocity > 0)
	                    	{
	                    		player.moveRelative(player.moveStrafing, 0, player.moveForward, jumpVelocity);
	                    	}
	                    	else player.moveRelative(0, 0, player.moveForward, 0.1F);
	                    	
	                    	PacketHandler.INSTANCE.sendToServer(new MessageOnDoubleJump());
	                    	((IMultiJump) boots.getItem()).onJump(boots);
	                    	
	                        ++jumpCount;
	                    }
	            	}
	            }
	            else if (jumpPressed)
	            {
	            	jumpPressed = false;
	            }
	
	            if (jumpCount != 0 && player.onGround)
	            {
	            	jumpCount = 0;
	            }
        	}
        }
    }
	
	@SubscribeEvent
    public void playerJump(LivingEvent.LivingJumpEvent evt)
    {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			jumpPressed = true;
		}
    }
}