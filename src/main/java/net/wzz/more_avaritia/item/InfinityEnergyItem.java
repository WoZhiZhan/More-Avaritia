package net.wzz.more_avaritia.item;

import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityEnergyItem extends Item {
    public InfinityEnergyItem() {
        super(new Item.Properties().stacksTo(1).durability(100).rarity(ModRarities.COSMIC).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.more_avaritia.infinity_energy.text.1"));
        tooltip.add(Component.translatable("item.more_avaritia.infinity_energy.text.2"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
