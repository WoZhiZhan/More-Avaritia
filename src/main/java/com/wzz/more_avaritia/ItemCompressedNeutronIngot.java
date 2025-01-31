package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCompressedNeutronIngot extends Item {
    public static ItemCompressedNeutronIngot itemCompressedNeutronIngot;
    public ItemCompressedNeutronIngot() {
        super();
        setUnlocalizedName("compressed_neutron_ingot");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:compressed_neutron_ingot");
        itemCompressedNeutronIngot = this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return "\u538b\u7f29\u4e2d\u5b50\u952d";
    }
}
