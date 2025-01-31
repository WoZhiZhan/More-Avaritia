package com.wzz.more_avaritia.ItemShander;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface ICosmicRenderItem {
    @SideOnly(Side.CLIENT)
    IIcon getMaskTexture(ItemStack paramItemStack, EntityPlayer paramEntityPlayer);

    @SideOnly(Side.CLIENT)
    float getMaskMultiplier(ItemStack paramItemStack, EntityPlayer paramEntityPlayer);
}
