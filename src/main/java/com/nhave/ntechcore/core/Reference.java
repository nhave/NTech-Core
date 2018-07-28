package com.nhave.ntechcore.core;

public class Reference
{
	public static final String LIBRARY_VERSION = "1.0.0";
    public static final String LOAD_BEFORE = "";
    
	public static final String MODID = "ntechcore";
    public static final String NAME = "NTech Core";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "required-after:nhlib@[" + LIBRARY_VERSION + ",)" + LOAD_BEFORE;
    
    public static final String GUIFACTORY = "com.nhave.ntechcore.client.gui.ModGuiFactory";
    public static final String CLIENT_PROXY = "com.nhave.ntechcore.core.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.nhave.ntechcore.core.proxy.CommonProxy";
}