package com.wzz.more_avaritia.ItemShander;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CosmicItemRenderer implements IItemRenderer {
    private float huehuehue = (float)milliTime() / 700.0F % 1.0F;

    private final float huehuehueStep = (float)rangeRemap(Math.sin(((float)milliTime() / 1200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);

    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return (helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING);
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        RenderItem r;
        Minecraft mc = Minecraft.getMinecraft();
        processLightLevel(type, item, data);
        switch (type) {
            case ENTITY:
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
                if (item.isOnItemFrame())
                    GL11.glTranslatef(0.0F, -0.3F, 0.01F);
                render(item, null);
                GL11.glPopMatrix();
                break;
            case EQUIPPED:
                render(item, (data[1] instanceof EntityPlayer) ? (EntityPlayer)data[1] : null);
                break;
            case EQUIPPED_FIRST_PERSON:
                render(item, (data[1] instanceof EntityPlayer) ? (EntityPlayer)data[1] : null);
                break;
            case INVENTORY:
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(3008);
                GL11.glDisable(2929);
                r = RenderItem.getInstance();
                r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);
                if (item.getItem() instanceof ICosmicRenderItem) {
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    RenderHelper.enableGUIStandardItemLighting();
                    GL11.glDisable(3008);
                    GL11.glDisable(2929);
                    ICosmicRenderItem icri = (ICosmicRenderItem)item.getItem();
                    CosmicRenderShenanigans.cosmicOpacity = icri.getMaskMultiplier(item, null);
                    CosmicRenderShenanigans.inventoryRender = true;
                    CosmicRenderShenanigans.useShader();
                    IIcon cosmicicon = icri.getMaskTexture(item, null);
                    GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
                    float minu = cosmicicon.getMinU();
                    float maxu = cosmicicon.getMaxU();
                    float minv = cosmicicon.getMinV();
                    float maxv = cosmicicon.getMaxV();
                    Tessellator t = Tessellator.instance;
                    t.startDrawingQuads();
                    t.addVertexWithUV(0.0D, 0.0D, 0.0D, minu, minv);
                    t.addVertexWithUV(0.0D, 16.0D, 0.0D, minu, maxv);
                    t.addVertexWithUV(16.0D, 16.0D, 0.0D, maxu, maxv);
                    t.addVertexWithUV(16.0D, 0.0D, 0.0D, maxu, minv);
                    t.draw();
                    CosmicRenderShenanigans.releaseShader();
                    CosmicRenderShenanigans.inventoryRender = false;
                }
                GL11.glEnable(3008);
                GL11.glEnable(32826);
                GL11.glEnable(2929);
                r.renderWithColor = true;
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                break;
        }
    }

    private static long milliTime() {
        return System.nanoTime() / 1000000L;
    }

    private static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }

    public void render(ItemStack item, EntityPlayer player) {
        int passes = 1;
        int rgb = 255;
        if (item.getItem().requiresMultipleRenderPasses())
            passes = item.getItem().getRenderPasses(item.getItemDamage());
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float scale = 0.0625F;
        float huehuehue = (float)milliTime() / 700.0F % 1.0F;
        float huehuehueStep = (float)rangeRemap(Math.sin(((float)milliTime() / 1200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
        for (int i = 0; i < passes; i++) {
            IIcon icon = getStackIcon(item, i, player);
            float f = icon.getMinU();
            float f1 = icon.getMaxU();
            float f2 = icon.getMinV();
            float f3 = icon.getMaxV();
            int colour = item.getItem().getColorFromItemStack(item, i);
            int c = 0x0 | Color.HSBtoRGB(huehuehue, 0.8F, 1.0F);
            float r = (c >> 16 & 0xFF) / 255.0F;
            float g = (c >> 8 & 0xFF) / 255.0F;
            float b = (c & 0xFF) / 255.0F;
            float a = (c >> 24 & 0xFF) / 255.0F;
            for (int u = 0; u <= rgb; u++) {
                b += u;
                g += u;
                r += u;
                GL11.glColor4f(r, g, b, a);
            }
            huehuehue += huehuehueStep;
            huehuehue %= 1.0F;
            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);
        }
        if (item.getItem() instanceof ICosmicRenderItem) {
            GL11.glDisable(3008);
            GL11.glDepthFunc(514);
            ICosmicRenderItem icri = (ICosmicRenderItem)item.getItem();
            CosmicRenderShenanigans.cosmicOpacity = icri.getMaskMultiplier(item, player);
            CosmicRenderShenanigans.useShader();
            IIcon cosmicicon = icri.getMaskTexture(item, player);
            float minu = cosmicicon.getMinU();
            float maxu = cosmicicon.getMaxU();
            float minv = cosmicicon.getMinV();
            float maxv = cosmicicon.getMaxV();
            ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, cosmicicon.getIconWidth(), cosmicicon.getIconHeight(), scale);
            CosmicRenderShenanigans.releaseShader();
            GL11.glDepthFunc(515);
            GL11.glEnable(3008);
        }
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void processLightLevel(ItemRenderType type, ItemStack item, Object... data) {
        EntityItem entityItem;
        EntityLivingBase ent;
        switch (type) {
            case ENTITY:
                entityItem = (EntityItem)data[1];
                if (entityItem != null)
                    CosmicRenderShenanigans.setLightFromLocation(entityItem.worldObj, MathHelper.floor_double(entityItem.posX), MathHelper.floor_double(entityItem.posY), MathHelper.floor_double(entityItem.posZ));
                return;
            case EQUIPPED:
                ent = (EntityLivingBase)data[1];
                if (ent != null)
                    CosmicRenderShenanigans.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
                return;
            case EQUIPPED_FIRST_PERSON:
                ent = (EntityLivingBase)data[1];
                if (ent != null)
                    CosmicRenderShenanigans.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
                return;
            case INVENTORY:
                CosmicRenderShenanigans.setLightLevel(1.2F);
                return;
        }
        CosmicRenderShenanigans.setLightLevel(1.0F);
    }

    public IIcon getStackIcon(ItemStack stack, int pass, EntityPlayer player) {
        return stack.getItem().getIcon(stack, pass);
    }
}
