package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityHoeItem extends morph.avaritia.item.tools.InfinityHoeItem {
    public InfinityHoeItem() {
        super(new Properties().tab(AvaritiaModContent.TAB).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(new TranslatableComponent("item.infinity_hoe.text.1"));
    }
}
