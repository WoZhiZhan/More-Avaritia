package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemNeutronAxe extends ItemAxe {
    public static ItemNeutronAxe itemNeutronAxe;
    public static Item.ToolMaterial enumTool = EnumHelper.addToolMaterial("neutron_axe", 70000, 5000, 40.0F, 30.0F, 30);
    public ItemNeutronAxe() {
        super(enumTool);
        setUnlocalizedName("neutron_axe");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:neutron_axe");
        itemNeutronAxe = this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return "\u4e2d\u5b50\u65a7";
    }
}
