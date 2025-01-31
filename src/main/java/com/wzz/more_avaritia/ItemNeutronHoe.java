package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemNeutronHoe extends ItemHoe {
    public static ItemNeutronHoe itemNeutronHoe;
    public static ToolMaterial enumTool = EnumHelper.addToolMaterial("neutron_hoe", 70000, 5000, 40.0F, 8.0F, 30);
    public ItemNeutronHoe() {
        super(enumTool);
        setUnlocalizedName("neutron_hoe");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:neutron_hoe");
        itemNeutronHoe = this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return "\u4e2d\u5b50\u9504";
    }
}
