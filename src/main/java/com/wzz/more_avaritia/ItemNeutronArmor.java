package com.wzz.more_avaritia;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemNeutronArmor extends ItemArmor {
  public static final ItemArmor.ArmorMaterial neutronArmor = EnumHelper.addArmorMaterial("neutron_armor", 50, new int[] { 6, 9, 8, 5 }, 3);
  
  public ItemNeutronArmor(String un, int type) {
    super(neutronArmor, 0,type);
    setUnlocalizedName(un);
    setTextureName("more_avaritia:"+un);
    setCreativeTab(Avaritia.tab);
  }
  
  public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
    return "more_avaritia:models/armor/neutron_layer_" + ((this.armorType == 2) ? "2" : "1") + ".png";
  }
}
