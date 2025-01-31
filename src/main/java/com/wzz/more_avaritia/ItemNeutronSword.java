package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemNeutronSword extends ItemSword {
    public static ItemNeutronSword itemNeutronSword;
    public static ToolMaterial enumTool = EnumHelper.addToolMaterial("neutron_sword", 70000, 0, 3000.0F, 60.0F, 30);
    public ItemNeutronSword() {
        super(enumTool);
        setUnlocalizedName("neutron_sword");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:neutron_sword");
        itemNeutronSword = this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return "\u4e2d\u5b50\u5251";
    }
}
