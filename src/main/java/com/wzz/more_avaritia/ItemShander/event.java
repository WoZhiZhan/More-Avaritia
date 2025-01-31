package com.wzz.more_avaritia.ItemShander;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class event {
    private static final int cosmicCount = 10;

    public static String[] cosmicTextures = new String[10];

    static {
        for (int ii = 0; ii < 10; ii++)
            cosmicTextures[ii] = "more_avaritia:cosmic" + ii;
    }

    public static FloatBuffer cosmicUVs = BufferUtils.createFloatBuffer(4 * cosmicTextures.length);

    public static IIcon[] cosmicIcons = new IIcon[cosmicTextures.length];

    @SubscribeEvent
    public void letsMakeAQuilt(TextureStitchEvent.Pre pre) {
        if (pre.map.getTextureType() != 1)
            return;
        for (int i = 0; i < cosmicTextures.length; i++) {
            IIcon icon = pre.map.registerIcon(cosmicTextures[i]);
            cosmicIcons[i] = icon;
        }
    }

    @SubscribeEvent
    public void weMadeAQuilt(TextureStitchEvent.Post post) {
        if (post.map.getTextureType() != 1)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
    }

    @SubscribeEvent
    public void pushTheCosmicFancinessToTheLimit(TickEvent.RenderTickEvent renderTickEvent) {
        if (renderTickEvent.phase == TickEvent.Phase.START) {
            cosmicUVs = BufferUtils.createFloatBuffer(4 * cosmicIcons.length);
            for (int i = 0; i < cosmicIcons.length; i++) {
                IIcon icon = cosmicIcons[i];
                cosmicUVs.put(icon.getMinU());
                cosmicUVs.put(icon.getMinV());
                cosmicUVs.put(icon.getMaxU());
                cosmicUVs.put(icon.getMaxV());
            }
            cosmicUVs.flip();
        }
    }

    /*@SubscribeEvent
    public void makeCosmicStuffLessDumbInGUIs(GuiScreenEvent.DrawScreenEvent.Pre pre) {
        CosmicRenderShenanigans.inventoryRender = true;
    }

    @SubscribeEvent
    public void finishMakingCosmicStuffLessDumbInGUIs(GuiScreenEvent.DrawScreenEvent.Post post) {
        CosmicRenderShenanigans.inventoryRender = false;
    }*/
}
