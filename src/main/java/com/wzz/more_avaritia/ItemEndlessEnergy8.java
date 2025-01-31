package com.wzz.more_avaritia;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.ItemResource;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.IHaloRenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemEndlessEnergy8 extends Item implements IHaloRenderItem {
    public static ItemEndlessEnergy8 itemEndlessEnergy;
    public ItemEndlessEnergy8() {
        super();
        setUnlocalizedName("endless_energy8");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:endless_energy");
        itemEndlessEnergy = this;
    }

    @SideOnly(Side.CLIENT)
    public boolean drawHalo(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getHaloTexture(ItemStack stack) {
        return ((ItemResource) LudicrousItems.resource).halo[0];
    }

    @SideOnly(Side.CLIENT)
    public int getHaloSize(ItemStack stack) {
        return 4;
    }

    @SideOnly(Side.CLIENT)
    public boolean drawPulseEffect(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getHaloColour(ItemStack stack) {
        return -16777216;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return RainbowText.Rainbow("\u65e0\u5c3d\u80fd\u6e90 8");
    }
}
