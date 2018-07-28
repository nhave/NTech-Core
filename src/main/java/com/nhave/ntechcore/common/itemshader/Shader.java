package com.nhave.ntechcore.common.itemshader;

import java.util.Map;

import com.google.common.collect.Maps;
import com.nhave.ntechcore.core.Reference;

import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;

public class Shader
{
	public static final Map<String, Shader> SHADERS = Maps.newHashMap();
	private NonNullList<Item> compatibleItems = NonNullList.create();
	private String name;
	private String resourceDomain;
	private int[] colors = new int[2];
	private int quality;
	
	public Shader(String name, String modid, int quality, int baseColor, int lightColor)
	{
		this.name = name;
		this.resourceDomain = modid;
		this.colors[0] = baseColor;
		this.colors[1] = lightColor;
		this.quality = quality;
		
		SHADERS.put(name, this);
	}
	
	public Shader(String name, int quality, int baseColor, int lightColor)
	{
		this(name, Reference.MODID, quality, baseColor, lightColor);
	}
	
	public Shader(String name, int baseColor, int lightColor)
	{
		this(name, 0, baseColor, lightColor);
	}
	
	public Shader(String name)
	{
		this(name, 16777215, 16777215);
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
	
	public int getColor(int layer)
	{
		return this.colors[Math.max(0, Math.min(1, layer))];
	}
	
	public Shader addCompatibleItems(Item... items)
	{
		for (int i = 0; i < items.length; ++i)
		{
			this.compatibleItems.add(items[i]);
		}
		return this;
	}
	
	public boolean isItemCompatible(Item item)
	{
		return item != null && compatibleItems.contains(item);
	}
	
	public NonNullList<Item> getCompatibleItems()
	{
		return this.compatibleItems;
	}
}