package net.wzz.more_avaritia.util;

import codechicken.lib.math.MathHelper;
import codechicken.lib.texture.TextureUtils;
import java.util.ArrayList;
import java.util.Random;
import morph.avaritia.client.ColourHelper;
import morph.avaritia.client.render.entity.ModelRendererWing;
import morph.avaritia.client.render.shader.CosmicShaderHelper;

import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.MoreAvaritiaMod;

@SideOnly(Side.CLIENT)
public class ModelArmorSuperInfinity extends ModelBiped {
    public static final ModelArmorSuperInfinity armorModel = new ModelArmorSuperInfinity(1.0F);
    public static final ModelArmorSuperInfinity legModel = (new ModelArmorSuperInfinity(0.5F)).setLegs(true);
    public static ResourceLocation eyeTex = new ResourceLocation("more_avaritia", "textures/models/infinity_armor_eyes.png");
    public static ResourceLocation wingTex = new ResourceLocation("more_avaritia", "textures/models/infinity_armor_wing.png");
    public static ResourceLocation wingGlowTex = new ResourceLocation("more_avaritia", "textures/models/infinity_armor_wingglow.png");
    public static int itempagewidth = 0;
    public static int itempageheight = 0;
    public boolean legs = false;
    public EntityEquipmentSlot currentSlot;
    private Random randy;
    private Overlay overlay;
    private Overlay invulnOverlay;
    private boolean invulnRender;
    private boolean showHat;
    private boolean showChest;
    private boolean showLeg;
    private boolean showFoot;
    private float expand;
    public ModelRenderer bipedLeftWing;
    public ModelRenderer bipedRightWing;

    public ModelArmorSuperInfinity(float expand) {
        super(expand, 0.0F, 64, 64);
        this.currentSlot = EntityEquipmentSlot.HEAD;
        this.randy = new Random();
        this.invulnRender = true;
        this.expand = expand;
        this.overlay = new Overlay(this, expand);
        this.invulnOverlay = new Overlay(this, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expand * 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public ModelArmorSuperInfinity setLegs(boolean islegs) {
        this.legs = islegs;
        int heightoffset = 0;
        int legoffset = islegs ? 32 : 0;
        this.bipedBody = new ModelRenderer(this, 16, 16 + legoffset);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, this.expand);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + (float)heightoffset, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16 + legoffset);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + (float)heightoffset, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16 + legoffset);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + (float)heightoffset, 0.0F);
        return this;
    }

    public void rebuildWings() {
        if (this.bipedBody.childModels == null) {
            this.bipedBody.childModels = new ArrayList();
        }

        if (this.bipedLeftWing != null) {
            this.bipedBody.childModels.remove(this.bipedLeftWing);
        }

        if (this.bipedRightWing != null) {
            this.bipedBody.childModels.remove(this.bipedRightWing);
        }

        this.bipedLeftWing = new ModelRendererWing(this, 0, 0);
        this.bipedLeftWing.mirror = true;
        this.bipedLeftWing.addBox(0.0F, -11.6F, 0.0F, 0, 32, 32);
        this.bipedLeftWing.setRotationPoint(-1.5F, 0.0F, 2.0F);
        this.bipedLeftWing.rotateAngleY = 1.2566371F;
        this.bipedBody.addChild(this.bipedLeftWing);
        this.bipedRightWing = new ModelRendererWing(this, 0, 0);
        this.bipedRightWing.addBox(0.0F, -11.6F, 0.0F, 0, 32, 32);
        this.bipedRightWing.setRotationPoint(1.5F, 0.0F, 2.0F);
        this.bipedRightWing.rotateAngleY = -1.2566371F;
        this.bipedBody.addChild(this.bipedRightWing);
    }
    private static long milliTime() {
        return System.nanoTime() / 100000000L;
    }
    public float[] colors = new float[]{0.45F, 0.45F, 0.5F};
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean isFlying = entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isFlying && entity.isAirBorne;
        super.render(entity, f, f1, f2, f3, f4, f5);
        Random randCol = new Random(milliTime());
        this.colors = new float[]{randCol.nextFloat(), randCol.nextFloat(),randCol.nextFloat()};
        float r = this.colors[0];
        float g = this.colors[1];
        float b = this.colors[2];
        GlStateManager.color(r,g,b, 1.0F);
        CosmicShaderHelper.useShader();
        TextureUtils.bindBlockTexture();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        if (this.invulnRender) {
            GlStateManager.color(r,g,b, 0.2F);
            this.invulnOverlay.render(entity, f, f1, f2, f3, f4, f5);
        }

        this.overlay.render(entity, f, f1, f2, f3, f4, f5);
        CosmicShaderHelper.releaseShader();
        mc.renderEngine.bindTexture(eyeTex);
        GlStateManager.disableLighting();
        mc.entityRenderer.disableLightmap();
        long time = mc.player.world.getWorldTime();
        this.setGems();
        double pulse = Math.sin((double)time / 10.0) * 0.5 + 0.5;
        double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
        GlStateManager.color(r,g,b, (float)(pulse_mag_sqr * 0.5));
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
        super.render(entity, f, f1, f2, f3, f4, f5);
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        if (this.invulnRender) {
            long frame = time / 3L;
            this.randy.setSeed(frame * 1723609L);
            float o = this.randy.nextFloat() * 6.0F;
            float[] col = ColourHelper.HSVtoRGB(o, 1.0F, 1.0F);
            GlStateManager.color(col[0], col[1], col[2], 1.0F);
            this.setEyes();
            super.render(entity, f, f1, f2, f3, f4, f5);
        }

        if (!CosmicShaderHelper.inventoryRender) {
            mc.entityRenderer.enableLightmap();
        }

        GlStateManager.enableLighting();
        GlStateManager.color(r,g,b, 1.0F);
        if (isFlying && !CosmicShaderHelper.inventoryRender) {
            this.setWings();
            mc.renderEngine.bindTexture(wingTex);
            super.render(entity, f, f1, f2, f3, f4, f5);
            CosmicShaderHelper.useShader();
            TextureUtils.bindBlockTexture();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.depthMask(false);
            this.overlay.render(entity, f, f1, f2, f3, f4, f5);
            CosmicShaderHelper.releaseShader();
            mc.renderEngine.bindTexture(wingGlowTex);
            GlStateManager.disableLighting();
            mc.entityRenderer.disableLightmap();
            GlStateManager.color(r,g,b, (float)(pulse_mag_sqr * 0.5));
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            super.render(entity, f, f1, f2, f3, f4, f5);
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            if (!CosmicShaderHelper.inventoryRender) {
                mc.entityRenderer.enableLightmap();
            }

            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    public void update(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot) {
        this.currentSlot = armorSlot;
        this.invulnRender = false;
        ItemStack hat = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack leg = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack foot = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        boolean hasHat = hat.getItem() == MoreAvaritiaMod.infinity_helmet;
        boolean hasChest = chest.getItem() == MoreAvaritiaMod.infinity_chestplate;
        boolean hasLeg = leg.getItem() == MoreAvaritiaMod.infinity_pants;
        boolean hasFoot = foot.getItem() == MoreAvaritiaMod.infinity_boots;
        if (armorSlot == EntityEquipmentSlot.HEAD && hasHat && hasChest && hasLeg && hasFoot) {
            this.invulnRender = true;
        }

        this.showHat = hasHat && armorSlot == EntityEquipmentSlot.HEAD;
        this.showChest = hasChest && armorSlot == EntityEquipmentSlot.CHEST;
        this.showLeg = hasLeg && armorSlot == EntityEquipmentSlot.LEGS;
        this.showFoot = hasFoot && armorSlot == EntityEquipmentSlot.FEET;
        this.bipedHead.showModel = this.showHat;
        this.bipedHeadwear.showModel = this.showHat;
        this.bipedBody.showModel = this.showChest || this.showLeg;
        this.bipedRightArm.showModel = this.showChest;
        this.bipedLeftArm.showModel = this.showChest;
        this.bipedRightLeg.showModel = this.showLeg || this.showFoot;
        this.bipedLeftLeg.showModel = this.showLeg || this.showFoot;
        this.overlay.bipedHead.showModel = this.showHat;
        this.overlay.bipedHeadwear.showModel = this.showHat;
        this.overlay.bipedBody.showModel = this.showChest || this.showLeg;
        this.overlay.bipedRightArm.showModel = this.showChest;
        this.overlay.bipedLeftArm.showModel = this.showChest;
        this.overlay.bipedRightLeg.showModel = this.showLeg || this.showFoot;
        this.overlay.bipedLeftLeg.showModel = this.showLeg || this.showFoot;
        this.bipedLeftWing.showModel = false;
        this.bipedRightWing.showModel = false;
        this.overlay.bipedLeftWing.showModel = false;
        this.overlay.bipedRightWing.showModel = false;
        this.isSneak = entityLiving.isSneaking();
        this.isRiding = entityLiving.isRiding();
        this.isChild = entityLiving.isChild();
        this.overlay.isSneak = entityLiving.isSneaking();
        this.overlay.isRiding = entityLiving.isRiding();
        this.overlay.isChild = entityLiving.isChild();
        this.invulnOverlay.isSneak = entityLiving.isSneaking();
        this.invulnOverlay.isRiding = entityLiving.isRiding();
        this.invulnOverlay.isChild = entityLiving.isChild();
        this.overlay.swingProgress = this.swingProgress;
        this.invulnOverlay.swingProgress = this.swingProgress;
        this.leftArmPose = ArmPose.EMPTY;
        this.rightArmPose = ArmPose.EMPTY;
        this.overlay.leftArmPose = ArmPose.EMPTY;
        this.overlay.rightArmPose = ArmPose.EMPTY;
        this.invulnOverlay.leftArmPose = ArmPose.EMPTY;
        this.invulnOverlay.rightArmPose = ArmPose.EMPTY;
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            ItemStack main_hand = player.getHeldItem(EnumHand.MAIN_HAND);
            if (main_hand != null) {
                this.rightArmPose = ArmPose.ITEM;
                this.overlay.rightArmPose = ArmPose.ITEM;
                this.invulnOverlay.rightArmPose = ArmPose.ITEM;
                if (player.getItemInUseCount() > 0) {
                    EnumAction enumaction = main_hand.getItemUseAction();
                    if (enumaction == EnumAction.BOW) {
                        this.rightArmPose = ArmPose.BOW_AND_ARROW;
                        this.overlay.rightArmPose = ArmPose.BOW_AND_ARROW;
                        this.invulnOverlay.rightArmPose = ArmPose.BOW_AND_ARROW;
                    } else if (enumaction == EnumAction.BLOCK) {
                        this.rightArmPose = ArmPose.BLOCK;
                        this.overlay.rightArmPose = ArmPose.BLOCK;
                        this.invulnOverlay.rightArmPose = ArmPose.BLOCK;
                    }
                }
            }

            ItemStack off_hand = player.getHeldItem(EnumHand.OFF_HAND);
            if (off_hand != null) {
                this.leftArmPose = ArmPose.ITEM;
                this.overlay.leftArmPose = ArmPose.ITEM;
                this.invulnOverlay.leftArmPose = ArmPose.ITEM;
                if (player.getItemInUseCount() > 0) {
                    EnumAction enumaction = off_hand.getItemUseAction();
                    if (enumaction == EnumAction.BOW) {
                        this.leftArmPose = ArmPose.BOW_AND_ARROW;
                        this.overlay.leftArmPose = ArmPose.BOW_AND_ARROW;
                        this.invulnOverlay.leftArmPose = ArmPose.BOW_AND_ARROW;
                    } else if (enumaction == EnumAction.BLOCK) {
                        this.leftArmPose = ArmPose.BLOCK;
                        this.overlay.leftArmPose = ArmPose.BLOCK;
                        this.invulnOverlay.leftArmPose = ArmPose.BLOCK;
                    }
                }
            }
        }

    }

    public void setRotationAngles(float f1, float speed, float ticks, float headYaw, float headPitch, float f6, Entity entity) {
        super.setRotationAngles(f1, speed, ticks, headYaw, headPitch, f6, entity);
        this.overlay.setRotationAngles(f1, speed, ticks, headYaw, headPitch, f6, entity);
        this.invulnOverlay.setRotationAngles(f1, speed, ticks, headYaw, headPitch, f6, entity);
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        if (manager.entityRenderMap.containsKey(entity.getClass())) {
            Render r = (Render)manager.entityRenderMap.get(entity.getClass());
            if (r instanceof RenderBiped) {
                ModelBiped m = (ModelBiped)((RenderBiped)r).getMainModel();
                copyBipedAngles(m, this);
            }
        }

    }

    public void setEyes() {
        this.bipedHead.showModel = false;
        this.bipedBody.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftArm.showModel = false;
        this.bipedRightLeg.showModel = false;
        this.bipedLeftLeg.showModel = false;
        this.bipedHeadwear.showModel = this.showHat;
    }

    public void setGems() {
        this.bipedHead.showModel = false;
        this.bipedHeadwear.showModel = false;
        this.bipedBody.showModel = this.legs ? false : this.showChest;
        this.bipedRightArm.showModel = this.legs ? false : this.showChest;
        this.bipedLeftArm.showModel = this.legs ? false : this.showChest;
        this.bipedRightLeg.showModel = this.legs ? this.showLeg : false;
        this.bipedLeftLeg.showModel = this.legs ? this.showLeg : false;
    }

    public void setWings() {
        this.bipedBody.showModel = this.legs ? false : this.showChest;
        this.bipedLeftWing.showModel = true;
        this.bipedRightWing.showModel = true;
        this.bipedHeadwear.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftArm.showModel = false;
        this.bipedRightLeg.showModel = false;
        this.bipedLeftLeg.showModel = false;
        this.bipedHeadwear.showModel = false;
        this.bipedHead.showModel = false;
        this.overlay.bipedBody.showModel = this.legs ? false : this.showChest;
        this.overlay.bipedLeftWing.showModel = true;
        this.overlay.bipedRightWing.showModel = true;
        this.overlay.bipedHead.showModel = false;
        this.overlay.bipedHeadwear.showModel = false;
    }

    public void rebuildOverlay() {
        this.rebuildWings();
        this.overlay.rebuild(AvaritiaTextures.INFINITY_ARMOR_MASK, AvaritiaTextures.INFINITY_ARMOR_MASK_WINGS);
        this.invulnOverlay.rebuild(AvaritiaTextures.INFINITY_ARMOR_MASK_INV, (TextureAtlasSprite)null);
    }

    public static void copyPartAngles(ModelRenderer source, ModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    public static void copyBipedAngles(ModelBiped source, ModelBiped dest) {
        copyPartAngles(source.bipedBody, dest.bipedBody);
        copyPartAngles(source.bipedHead, dest.bipedHead);
        copyPartAngles(source.bipedHeadwear, dest.bipedHeadwear);
        copyPartAngles(source.bipedLeftArm, dest.bipedLeftArm);
        copyPartAngles(source.bipedLeftLeg, dest.bipedLeftLeg);
        copyPartAngles(source.bipedRightArm, dest.bipedRightArm);
        copyPartAngles(source.bipedRightLeg, dest.bipedRightLeg);
    }

    public class Overlay extends ModelBiped {
        public ModelArmorSuperInfinity parent;
        public float expand;
        public ModelRenderer bipedLeftWing;
        public ModelRenderer bipedRightWing;

        public Overlay(ModelArmorSuperInfinity parent, float expand) {
            this.parent = parent;
            this.expand = expand;
        }

        public void rebuild(TextureAtlasSprite icon, TextureAtlasSprite wingicon) {
            int ox = MathHelper.floor(icon.getMinU() * (float) ModelArmorSuperInfinity.itempagewidth);
            int oy = MathHelper.floor(icon.getMinV() * (float) ModelArmorSuperInfinity.itempageheight);
            float heightoffset = 0.0F;
            int legoffset = this.parent.legs ? 32 : 0;
            this.textureWidth = ModelArmorSuperInfinity.itempagewidth;
            this.textureHeight = ModelArmorSuperInfinity.itempageheight;
            this.bipedHead = new ModelRenderer(this, 0 + ox, 0 + legoffset + oy);
            this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, this.expand);
            this.bipedHead.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
            this.bipedHeadwear = new ModelRenderer(this, 32 + ox, 0 + legoffset + oy);
            this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, this.expand * 0.5F);
            this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
            this.bipedBody = new ModelRenderer(this, 16 + ox, 16 + legoffset + oy);
            this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, this.expand);
            this.bipedBody.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40 + ox, 16 + legoffset + oy);
            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + heightoffset, 0.0F);
            this.bipedLeftArm = new ModelRenderer(this, 40 + ox, 16 + legoffset + oy);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + heightoffset, 0.0F);
            this.bipedRightLeg = new ModelRenderer(this, 0 + ox, 16 + legoffset + oy);
            this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + heightoffset, 0.0F);
            this.bipedLeftLeg = new ModelRenderer(this, 0 + ox, 16 + legoffset + oy);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + heightoffset, 0.0F);
            if (wingicon != null) {
                int oxw = MathHelper.floor(wingicon.getMinU() * (float) ModelArmorSuperInfinity.itempagewidth);
                int oyw = MathHelper.floor(wingicon.getMinV() * (float) ModelArmorSuperInfinity.itempageheight);
                if (this.bipedBody.childModels == null) {
                    this.bipedBody.childModels = new ArrayList();
                }

                if (this.bipedLeftWing != null) {
                    this.bipedBody.childModels.remove(this.bipedLeftWing);
                }

                if (this.bipedRightWing != null) {
                    this.bipedBody.childModels.remove(this.bipedRightWing);
                }

                this.bipedLeftWing = new ModelRendererWing(this, oxw, oyw);
                this.bipedLeftWing.mirror = true;
                this.bipedLeftWing.addBox(0.0F, -11.6F, 0.0F, 0, 32, 32);
                this.bipedLeftWing.setRotationPoint(-1.5F, 0.0F, 2.0F);
                this.bipedLeftWing.rotateAngleY = 1.2566371F;
                this.bipedBody.addChild(this.bipedLeftWing);
                this.bipedRightWing = new ModelRendererWing(this, oxw, oyw);
                this.bipedRightWing.addBox(0.0F, -11.6F, 0.0F, 0, 32, 32);
                this.bipedRightWing.setRotationPoint(1.5F, 0.0F, 2.0F);
                this.bipedRightWing.rotateAngleY = -1.2566371F;
                this.bipedBody.addChild(this.bipedRightWing);
            }

        }

        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            ModelArmorSuperInfinity.copyBipedAngles(this.parent, this);
            super.render(entity, f, f1, f2, f3, f4, f5);
        }

        public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entity) {
            super.setRotationAngles(f1, f2, f3, f4, f5, f6, entity);
            RenderManager manager = Minecraft.getMinecraft().getRenderManager();
            if (manager.entityRenderMap.containsKey(entity.getClass())) {
                Render r = (Render)manager.entityRenderMap.get(entity.getClass());
                if (r instanceof RenderBiped) {
                    ModelBiped m = (ModelBiped)((RenderBiped)r).getMainModel();
                    ModelArmorSuperInfinity.copyBipedAngles(m, this);
                }
            }

        }
    }
}
