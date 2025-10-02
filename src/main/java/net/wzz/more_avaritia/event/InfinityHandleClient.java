package net.wzz.more_avaritia.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CrossbowItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.wzz.more_avaritia.entity.model.InfinityArmorModel;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.item.UniverseHeartItem;
import net.wzz.more_avaritia.item.bow.InfinityBowItem;
import net.wzz.more_avaritia.item.bow.NeutronBowItem;
import net.wzz.more_avaritia.item.tools.InfinityGodSwordItem;
import net.wzz.more_avaritia.item.tools.InfinityPickaxeItem;
import net.wzz.more_avaritia.item.tools.InfinityShovelItem;
import net.wzz.more_avaritia.item.tools.InfinitySwordItem;
import net.wzz.more_avaritia.util.InfinityUtils;
import net.wzz.more_avaritia.util.RainbowText;

import static committee.nova.mods.avaritia.init.handler.ItemOverrideHandler.setPropertyOverride;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InfinityHandleClient {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        int x;
        if (event.getItemStack().getItem() instanceof InfinitySwordItem) {
            for(x = 0; x < event.getToolTip().size(); ++x) {
                if ((event.getToolTip().get(x)).getString().contains(I18n.get("attribute.name.generic.attack_damage"))) {
                    event.getToolTip().set(x, Component.literal("+").withStyle(ChatFormatting.BLUE).append(Component.literal(RainbowText.makeFabulous(I18n.get("tooltip.infinity")))).append(" ").append(Component.translatable("tooltip.infinity.desc").withStyle(ChatFormatting.BLUE)));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.INFINITY_PICKAXE.get(), InfinityUtils.rl("hammer"), (itemStack, world, livingEntity, d) -> {
                if (itemStack.getItem() instanceof InfinityPickaxeItem) {
                    return itemStack.getOrCreateTag().getBoolean("hammer") ? 1.0F : 0.0F;
                } else {
                    return 0.0F;
                }
            });
        });
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.INFINITY_BOW.get(), InfinityUtils.rl("tracer"), (itemStack, world, livingEntity, d) -> {
                if (itemStack.getItem() instanceof InfinityBowItem) {
                    return itemStack.getOrCreateTag().getBoolean("tracer") ? 1.0F : 0.0F;
                } else {
                    return 0.0F;
                }
            });
        });
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.INFINITY_BOW.get(), InfinityUtils.rl("pull"), (itemStack, world, livingEntity, d) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return CrossbowItem.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(itemStack);
                }
            });
        });
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.INFINITY_BOW.get(), InfinityUtils.rl("pulling"), (itemStack, world, livingEntity, d) -> {
                if (!(itemStack.getItem() instanceof InfinityBowItem)) {
                    return 0.0F;
                } else {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
                }
            });
        });
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.INFINITY_SHOVEL.get(), InfinityUtils.rl("destroyer"), (itemStack, world, livingEntity, d) -> {
                if (itemStack.getItem() instanceof InfinityShovelItem) {
                    return itemStack.getOrCreateTag().getBoolean("destroyer") ? 1.0F : 0.0F;
                } else {
                    return 0.0F;
                }
            });
        });
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.NEUTRON_BOW.get(), InfinityUtils.rl("pull"), (itemStack, world, livingEntity, d) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return CrossbowItem.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(itemStack);
                }
            });
        });
        event.enqueueWork(() -> {
            setPropertyOverride(MoreAvaritiaModItems.NEUTRON_BOW.get(), InfinityUtils.rl("pulling"), (itemStack, world, livingEntity, d) -> {
                if (!(itemStack.getItem() instanceof NeutronBowItem)) {
                    return 0.0F;
                } else {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
                }
            });
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Color e) {
        if (e.getItemStack().getItem() instanceof UniverseHeartItem) {
            long time = System.currentTimeMillis() / 60;
            float factor = (float)(Math.sin(time * 0.1) * 0.5 + 0.5);
            int red = (int)(255 * (1 - factor));
            int blue = (int)(255 * factor);
            int green = 216;
            int alpha = 255;
            int startColor = (alpha << 24) | (red << 16) | (green << 8) | 0xE6;
            int endColor = (alpha << 24) | (blue << 16) | (green << 8) | 0xFF;
            e.setBackgroundStart(startColor);
            e.setBackgroundEnd(endColor);
        }
        if (e.getItemStack().getItem() instanceof InfinityGodSwordItem) {
            int startColor = 0xFF0000 | 0xFF000000;
            e.setBackgroundStart(startColor);
            e.setBorderStart(startColor);
            e.setBackgroundEnd(0);
            e.setBorderEnd(0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void addPlayerLayer(EntityRenderersEvent.AddLayers event) {
        addLayer(event, "default");
        addLayer(event, "slim");
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("all")
    private static void addLayer(EntityRenderersEvent.AddLayers e, String s) {
        LivingEntityRenderer entityRenderer = e.getSkin(s);
        if (entityRenderer != null) {
            entityRenderer.addLayer(new InfinityArmorModel.PlayerRender(entityRenderer));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        //InfinityUtils.updateItemColor(MoreAvaritiaModItems.INFINITY_GOD_SWORD.get(), 0, 0.3f);
    }

    @SubscribeEvent
    public static void screenOpen(ScreenEvent.Opening event) {
        if (InfinityUtils.hasInfinityArmor(Minecraft.getInstance().player) && event.getScreen() instanceof DeathScreen) {
            event.setCanceled(true);
            event.setNewScreen(event.getScreen());
        }
    }

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Render event) {
        if (InfinityUtils.hasInfinityArmor(Minecraft.getInstance().player) && event.getScreen() instanceof DeathScreen) {
            event.setCanceled(true);
            Minecraft.getInstance().setScreen(null);
        }
    }
}
