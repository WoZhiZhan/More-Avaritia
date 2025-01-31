package net.wzz.more_avaritia.item;

import morph.avaritia.entity.EndestPearlEntity;
import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityShovelItem extends morph.avaritia.item.tools.InfinityShovelItem {
    public InfinityShovelItem() {
        super(new Properties().tab(AvaritiaModContent.TAB).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(new TranslatableComponent("infinity_shovel.text.1"));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.isShiftKeyDown()) {
            if (!entity.level.isClientSide && !stack.getOrCreateTag().getBoolean("destroyer") && !entity.getPersistentData().getBoolean("inModeTime")) {
                EndestPearlEntity thrown = new EndestPearlEntity(entity.level, entity);
                thrown.setItem(new ItemStack(MoreAvaritiaModItems.INFINITY_SINGULARITY.get()));
                thrown.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.5F, 1.0F);
                entity.level.addFreshEntity(thrown);
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        p_41406_.getPersistentData().putBoolean("inModeTime", p_41404_.getOrCreateTag().getBoolean("destroyer"));
    }
}
