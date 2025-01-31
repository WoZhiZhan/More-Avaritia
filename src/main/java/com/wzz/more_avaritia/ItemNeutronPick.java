package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemNeutronPick extends ItemPickaxe {
    public static ItemNeutronPick itemNeutronPick;
    public static ToolMaterial enumTool = EnumHelper.addToolMaterial("neutron_pick", 70000, 5000, 40.0F, 15.0F, 30);
    public ItemNeutronPick() {
        super(enumTool);
        setUnlocalizedName("neutron_pick");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:neutron_pick");
        itemNeutronPick = this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return "\u4e2d\u5b50\u9550";
    }
}
