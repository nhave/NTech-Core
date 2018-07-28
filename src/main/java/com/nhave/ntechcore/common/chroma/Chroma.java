package com.nhave.ntechcore.common.chroma;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.nhave.ntechcore.api.item.IChromaAcceptor;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.item.Item;

public class Chroma
{
	public static final Map<String, Chroma> CHROMAS = Maps.newHashMap();
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	private String name;
	private String resourceDomain;
	private int quality;
	private int color;
	
	public Chroma(String name, int color, String modid, int quality)
	{
		this.name = name;
		this.resourceDomain = modid;
		this.quality = quality;
		this.color = color;
		
		CHROMAS.put(name, this);
	}
	
	public Chroma(String name, int color, int quality)
	{
		this(name, color, Reference.MODID, quality);
	}
	
	public Chroma(String name, int color)
	{
		this(name, color, 0);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getResourceDomain()
	{
		return this.resourceDomain;
	}
	
	public int getQuality()
	{
		return this.quality;
	}
	
	public int getColor()
	{
		return this.color;
	}
	
	public static void createList()
	{
		for (Item item : Item.REGISTRY)
		{
			if (item instanceof IChromaAcceptor)
			{
				ITEMS.add(item);
			}
		}
	}
}