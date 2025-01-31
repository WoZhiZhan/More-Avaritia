package net.wzz.more_avaritia.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.wzz.more_avaritia.util.MoreAvaritiaRegister;

public class HasItemFromSlot {
    public static boolean hasArmor(EntityLivingBase living) {
        if (!(living instanceof EntityPlayer))
            return false;
        ItemStack hat = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack leg = living.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack foot = living.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        boolean hasHat = hat.getItem() == MoreAvaritiaRegister.infinity_helmet;
        boolean hasChest = chest.getItem() == MoreAvaritiaRegister.infinity_chestplate;
        boolean hasLeg = leg.getItem() == MoreAvaritiaRegister.infinity_pants;
        boolean hasFoot = foot.getItem() == MoreAvaritiaRegister.infinity_boots;
        return hasHat && hasChest && hasLeg && hasFoot;
    }
}
