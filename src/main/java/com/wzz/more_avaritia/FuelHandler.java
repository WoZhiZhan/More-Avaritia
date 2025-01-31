package com.wzz.more_avaritia;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.getItem() instanceof ItemEndlessEnergy)
            return 1000000000;
        return 0;
    }

}