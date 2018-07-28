package com.nhave.ntechcore.common.itemblock;

import com.nhave.ntechcore.common.block.BlockBase;
import com.nhave.rpgtooltips.common.item.EnumItemQuality;
import com.nhave.rpgtooltips.common.item.IItemQuality;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
    @Optional.Interface(iface = "com.nhave.rpgtooltips.common.item.IItemQuality", modid = "rpgtooltips")
})
public class ItemBlockBase extends ItemBlock implements IItemQuality
{
	public ItemBlockBase(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	@Optional.Method(modid = "rpgtooltips")
	@Override
	public EnumItemQuality getItemQuality(ItemStack stack)
	{
		switch (((BlockBase) getBlock()).getQuality(stack))
		{
		case 0:
			return EnumItemQuality.COMMON;
		case 1:
			return EnumItemQuality.UNCOMMON;
		case 2:
			return EnumItemQuality.RARE;
		case 3:
			return EnumItemQuality.EPIC;
		case 4:
			return EnumItemQuality.LEGENDARY;
		case 5:
			return EnumItemQuality.RELIC;
		case 6:
			return EnumItemQuality.CREATIVE;
		default:
			return EnumItemQuality.COMMON;
		}
	}
}