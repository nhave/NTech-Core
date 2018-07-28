package com.nhave.ntechcore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageResetFallDistance implements IMessage, IMessageHandler<MessageResetFallDistance, IMessage>
{
	@Override
	public IMessage onMessage(MessageResetFallDistance message, MessageContext ctx)
	{
		EntityPlayer entityPlayer = ctx.getServerHandler().player;
        
        if (entityPlayer != null)
        {
        	entityPlayer.fallDistance = 0;
        }
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {}
	
	@Override
	public void toBytes(ByteBuf buf) {}
}