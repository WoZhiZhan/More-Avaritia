package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemNeutronShovel extends ItemSpade {
    public static ItemNeutronShovel itemNeutronShovel;
    public static ToolMaterial enumTool = EnumHelper.addToolMaterial("neutron_shovel", 70000, 5000, 40.0F, 10.0F, 30);
    public ItemNeutronShovel() {
        super(enumTool);
        setUnlocalizedName("neutron_shovel");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:neutron_shovel");
        itemNeutronShovel = this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return "\u4e2d\u5b50\u94f2";
    }
}
