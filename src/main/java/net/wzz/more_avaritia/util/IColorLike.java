package net.wzz.more_avaritia.util;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public interface IColorLike {
    default int getColor(int i) {
        return -1;
    }

    default int getColor(ItemStack s, int i) {
        return this.getColor(i);
    }
    class ItemColors implements ItemColor {
        public ItemColors() {
        }

        @Override
        public int getColor(ItemStack itemStack, int i) {
            return ((IColorLike)itemStack.getItem()).getColor(itemStack, i);
        }
    }
}