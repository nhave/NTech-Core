package com.nhave.ntechcore.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("ntechcore");
	
    public static void init()
    {
        //INSTANCE.registerMessage(MessageResetFallDistance.class, MessageResetFallDistance.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageOnDoubleJump.class, MessageOnDoubleJump.class, 0, Side.SERVER);
    }
}