package net.wzz.more_avaritia.item;

import codechicken.lib.item.SimpleArmorMaterial;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import morph.avaritia.init.AvaritiaModContent;
import morph.avaritia.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.wzz.more_avaritia.client.InfinityArmorModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SuperInfinityArmorItem extends ArmorItem {
    private static final ArmorMaterial INFINITY = new SimpleArmorMaterial(new int[]{0, 0, 0, 0}, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ARMOR_EQUIP_NETHERITE, Ingredient::of, "more_avaritia:super_infinity", 1.0F, 100.0F);
    public SuperInfinityArmorItem(EquipmentSlot p_40387_) {
        super(INFINITY, p_40387_, new Properties().fireResistant().tab(AvaritiaModContent.TAB));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "more_avaritia:textures/models/infinity_armor.png";
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (entity.getAge() >= 0)
            entity.setExtendedLifetime();
        return super.onEntityItemUpdate(stack, entity);
    }

    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (this.slot == EquipmentSlot.HEAD) {
            player.setAirSupply(300);
            player.getFoodData().eat(20, 20.0F);
            MobEffectInstance nv = player.getEffect(MobEffects.NIGHT_VISION);
            if (nv == null) {
                nv = new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false);
                player.addEffect(nv);
            } else {
                nv.update(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
            }
        } else if (this.slot == EquipmentSlot.CHEST) {
            (player.getAbilities()).mayfly = true;
            List<MobEffectInstance> effects = Lists.newArrayList(player.getActiveEffects());
            for (MobEffectInstance effectInstance : Collections2.filter(effects, e -> (e.getEffect().getCategory() == MobEffectCategory.HARMFUL)))
                player.removeEffect(effectInstance.getEffect());
        } else if (this.slot == EquipmentSlot.LEGS &&
                player.isOnFire()) {
            player.clearFire();
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (this.slot == EquipmentSlot.FEET) {
            list.add(new TextComponent(""));
            list.add((new TextComponent("+"))
                    .withStyle(ChatFormatting.BLUE)
                    .append((Component) TextUtils.makeSANIC("SANIC")
                            .withStyle(ChatFormatting.ITALIC)
                            .append((Component)(new TextComponent("% Speed"))
                                    .withStyle(ChatFormatting.BLUE))));
        }
        super.appendHoverText(stack, level, list, flag);
    }

    public boolean canBeHurtBy(DamageSource source) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            public HumanoidModel getArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlot armorSlot, HumanoidModel _deafult) {
                InfinityArmorModel model = (armorSlot == EquipmentSlot.LEGS) ? new InfinityArmorModel(InfinityArmorModel.createMeshes(new CubeDeformation(1.0F), 0.0F, true).getRoot().bake(64, 64)) : new InfinityArmorModel(InfinityArmorModel.createMeshes(new CubeDeformation(1.0F), 0.0F, false).getRoot().bake(64, 64));
                model.update(entityLiving, itemstack, armorSlot);
                return model;
            }
        });
    }

    public static class SuperInfinityHead extends SuperInfinityArmorItem {
        public SuperInfinityHead() {
            super(EquipmentSlot.HEAD);
        }

        @Override
        public Component getName(ItemStack p_41458_) {
            return new TranslatableComponent("item.more_avaritia.super_infinity_head");
        }
    }

    public static class SuperInfinityChest extends SuperInfinityArmorItem {

        public SuperInfinityChest() {
            super(EquipmentSlot.CHEST);
        }
        @Override
        public Component getName(ItemStack p_41458_) {
            return new TranslatableComponent("item.more_avaritia.super_infinity_chest");
        }
    }

    public static class SuperInfinityLeg extends SuperInfinityArmorItem {

        public SuperInfinityLeg() {
            super(EquipmentSlot.LEGS);
        }

        @Override
        public Component getName(ItemStack p_41458_) {
            return new TranslatableComponent("item.more_avaritia.super_infinity_legs");
        }
    }

    public static class SuperInfinityBoots extends SuperInfinityArmorItem {

        public SuperInfinityBoots() {
            super(EquipmentSlot.FEET);
        }

        @Override
        public Component getName(ItemStack p_41458_) {
            return new TranslatableComponent("item.more_avaritia.super_infinity_boots");
        }
    }
}
