package net.wzz.more_avaritia.event;

import codechicken.lib.render.shader.CCShaderInstance;
import codechicken.lib.render.shader.CCUniform;
import codechicken.lib.texture.SpriteRegistryHelper;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.item.InfinityBowItem;

import java.util.Objects;

public class MoreClientInit {
    public static TextureAtlasSprite WING;
    public static TextureAtlasSprite WING_GLOW;
    public static CCShaderInstance cosmicShader2;
    public static CCShaderInstance cosmicShader;
    public static CCUniform cosmicTime;

    public static CCUniform cosmicYaw;

    public static CCUniform cosmicPitch;

    public static CCUniform cosmicExternalScale;

    public static CCUniform cosmicOpacity;

    public static CCUniform cosmicUVs;

    public static CCUniform cosmicTime2;

    public static CCUniform cosmicYaw2;

    public static CCUniform cosmicPitch2;

    public static CCUniform cosmicExternalScale2;

    public static CCUniform cosmicOpacity2;

    public static CCUniform cosmicUVs2;
    public static TextureAtlasSprite[] COSMIC_SPRITES = new TextureAtlasSprite[2];
    public static SpriteRegistryHelper WING_HELPER = new SpriteRegistryHelper();
    public static TextureAtlasSprite[] MASK_SPRITES_INV = new TextureAtlasSprite[1];
    public static TextureAtlasSprite[] MASK_SPRITES = new TextureAtlasSprite[1];
    public static TextureAtlasSprite[] WING_SPRITES = new TextureAtlasSprite[1];
    public static TextureAtlasSprite COSMIC;
    private static final SpriteRegistryHelper SPRITE_HELPER = new SpriteRegistryHelper();
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(MoreClientInit::onRegisterShaders);
        bus.addListener(MoreClientInit::clientInit);
        MinecraftForge.EVENT_BUS.addListener(MoreClientInit::onRenderTick);
        MinecraftForge.EVENT_BUS.addListener(MoreClientInit::clientTick);
        MinecraftForge.EVENT_BUS.addListener(MoreClientInit::renderTick);
        SPRITE_HELPER.addIIconRegister((registrar) -> {
            registrar.registerSprite(shader("cosmic_0"), (e) -> {
                COSMIC = e;
                COSMIC_SPRITES[0] = e;
            });
        });
        MASK_HELPER.addIIconRegister((registrar) -> {
            registrar.registerSprite(mask("mask"), (e) -> {
                MASK = e;
                MASK_SPRITES[0] = e;
            });
        });
        MASK_HELPER_INV.addIIconRegister((registrar) -> {
            registrar.registerSprite(mask("mask_inv"), (e) -> {
                MASK_INV = e;
                MASK_SPRITES_INV[0] = e;
            });
        });
        WING_HELPER.addIIconRegister((registrar) -> {
            registrar.registerSprite(mask("mask_wings"), (e) -> {
                WING = e;
                WING_SPRITES[0] = e;
            });
        });
        SPRITE_HELPER.addIIconRegister((registrar) -> {
            registrar.registerSprite(models("infinity_armor_wing"), (e) -> {
                TextureAtlasSprite[] var10000 = COSMIC_SPRITES;
                WING = e;
                var10000[0] = e;
            });
            registrar.registerSprite(models("infinity_armor_wingglow"), (e) -> {
                TextureAtlasSprite[] var10000 = COSMIC_SPRITES;
                WING_GLOW = e;
                var10000[1] = e;
            });
        });
    }
    static ResourceLocation mask(String path) {
        return new ResourceLocation("more_avaritia", "models/infinity_armor_" + path);
    }

    public static boolean inventoryRender = false;
    static int renderTime;
    static float renderFrame;
    public static TextureAtlasSprite MASK;
    public static TextureAtlasSprite MASK_INV;
    public static SpriteRegistryHelper MASK_HELPER = new SpriteRegistryHelper();
    public static SpriteRegistryHelper MASK_HELPER_INV = new SpriteRegistryHelper();
    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(CCShaderInstance.create(event.getResourceManager(), new ResourceLocation("more_avaritia", "cosmic"), DefaultVertexFormat.BLOCK), (e) -> {
            cosmicShader = (CCShaderInstance)e;
            cosmicTime = (CCUniform) Objects.<Uniform>requireNonNull(cosmicShader.getUniform("time"));
            cosmicYaw = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader.getUniform("yaw"));
            cosmicPitch = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader.getUniform("pitch"));
            cosmicExternalScale = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader.getUniform("externalScale"));
            cosmicOpacity = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader.getUniform("opacity"));
            cosmicUVs = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader.getUniform("cosmicuvs"));
            cosmicShader.onApply(() -> {
                cosmicTime.set((float)renderTime + renderFrame);
            });
        });
        event.registerShader(CCShaderInstance.create(event.getResourceManager(), new ResourceLocation("more_avaritia", "cosmic"), DefaultVertexFormat.BLOCK), (e) -> {
            cosmicShader2 = (CCShaderInstance)e;
            cosmicTime2 = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader2.getUniform("time"));
            cosmicYaw2 = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader2.getUniform("yaw"));
            cosmicPitch2 = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader2.getUniform("pitch"));
            cosmicExternalScale2 = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader2.getUniform("externalScale"));
            cosmicOpacity2 = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader2.getUniform("opacity"));
            cosmicUVs2 = (CCUniform)Objects.<Uniform>requireNonNull(cosmicShader2.getUniform("cosmicuvs"));
            cosmicShader2.onApply(() -> {
                cosmicTime2.set((float)renderTime + renderFrame);
            });
        });
    }

    private static void clientInit(FMLClientSetupEvent event) {
        ItemProperties.register(MoreAvaritiaModItems.INFINITY_BOW.get(), new ResourceLocation("more_avaritia", "pull"), (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float) InfinityBowItem.DRAW_TIME;
            }
        });
        ItemProperties.register(MoreAvaritiaModItems.INFINITY_BOW.get(), new ResourceLocation("more_avaritia", "pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        ItemProperties.register(MoreAvaritiaModItems.NEUTRON_BOW.get(), new ResourceLocation("more_avaritia", "pull"), (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 8f;
            }
        });
        ItemProperties.register(MoreAvaritiaModItems.NEUTRON_BOW.get(), new ResourceLocation("more_avaritia", "pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        ItemProperties.register(MoreAvaritiaModItems.INFINITY_PICKAXE.get(), new ResourceLocation("more_avaritia", "hammer"), (stack, level, entity, i) -> {
            return stack.hasTag() && stack.getOrCreateTag().getBoolean("hammer") ? 1.0F : 0.0F;
        });
        ItemProperties.register(MoreAvaritiaModItems.INFINITY_SHOVEL.get(), new ResourceLocation("more_avaritia", "destroyer"), (stack, level, entity, i) -> {
            return stack.hasTag() && stack.getOrCreateTag().getBoolean("destroyer") ? 1.0F : 0.0F;
        });
    }

    private static ResourceLocation models(String path) {
        return new ResourceLocation("more_avaritia", "models/" + path);
    }

    private static ResourceLocation shader(String path) {
        return new ResourceLocation("more_avaritia", "shader/" + path);
    }
    private static final float[] COSMIC_UVS = new float[40];
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.END) {
            ++renderTime;
        }
    }

    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.START) {
            renderFrame = event.renderTickTime;
        }
    }
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (int i = 0; i < COSMIC_SPRITES.length; i++) {
                TextureAtlasSprite sprite = COSMIC_SPRITES[i];
                COSMIC_UVS[i * 4] = sprite.getU0();
                COSMIC_UVS[i * 4 + 1] = sprite.getV0();
                COSMIC_UVS[i * 4 + 2] = sprite.getU1();
                COSMIC_UVS[i * 4 + 3] = sprite.getV1();
            }
            if (cosmicUVs != null)
                cosmicUVs.set(COSMIC_UVS);
            if (cosmicUVs2 != null)
                cosmicUVs2.set(COSMIC_UVS);
        }
    }
}
