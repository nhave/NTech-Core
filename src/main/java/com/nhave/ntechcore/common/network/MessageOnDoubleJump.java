package com.nhave.ntechcore.common.network;

import com.nhave.ntechcore.api.item.armor.IMultiJump;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOnDoubleJump implements IMessage, IMessageHandler<MessageOnDoubleJump, IMessage>
{
	@Override
	public IMessage onMessage(MessageOnDoubleJump message, MessageContext ctx)
	{
		EntityPlayer entityPlayer = ctx.getServerHandler().player;
        
        if (entityPlayer != null)
        {
        	entityPlayer.fallDistance = 0;
            ItemStack boots = entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (boots.getItem() instanceof IMultiJump)
            {
            	((IMultiJump) boots.getItem()).onJump(boots);
            }
        }
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {}
	
	@Override
	public void toBytes(ByteBuf buf) {}
}