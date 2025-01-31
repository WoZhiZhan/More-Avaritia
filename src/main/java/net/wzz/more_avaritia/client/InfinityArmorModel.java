package net.wzz.more_avaritia.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import morph.avaritia.client.AvaritiaShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.wzz.more_avaritia.event.MoreClientInit;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.util.MathHelper;

import java.util.Random;

public class InfinityArmorModel extends HumanoidModel {
  String s = "more_avaritia:textures/models/";
  ResourceLocation eyeTex;
  ResourceLocation wingTex;
  ResourceLocation wingGlowTex;
  static boolean invulnRender;
  static boolean playerFlying;
  static boolean player;
  static boolean legs = true;
  Minecraft mc;
  MultiBufferSource buf;
  Random randy;
  HumanoidModel invuln;
  ModelPart bipedLeftWing;
  ModelPart bipedRightWing;

  RenderType glow(ResourceLocation tex) {
    return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
  }

  RenderType mask(ResourceLocation tex) {
    return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> {
      return AvaritiaShaders.cosmicShader;
    })).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
  }

  static RenderType mask2(ResourceLocation tex) {
    return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> {
      return MoreClientInit.cosmicShader2;
    })).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).createCompositeState(true));
  }

  public InfinityArmorModel(ModelPart r, int x) {
    super(createMesh(new CubeDeformation(1.0F), 0.0F).getRoot().bake(64, 64));
    this.eyeTex = new ResourceLocation(this.s + "infinity_armor_eyes.png");
    this.wingTex = new ResourceLocation(this.s + "infinity_armor_wing.png");
    this.wingGlowTex = new ResourceLocation(this.s + "infinity_armor_wingglow.png");
    this.mc = Minecraft.getInstance();
    this.buf = this.mc.renderBuffers().bufferSource();
    this.randy = new Random();
    this.invuln = new HumanoidModel(createMesh(new CubeDeformation(0.0F), 0.0F).getRoot().bake(64, 64));
  }

  public InfinityArmorModel(ModelPart r) {
    super(r);
    this.eyeTex = new ResourceLocation(this.s + "infinity_armor_eyes.png");
    this.wingTex = new ResourceLocation(this.s + "infinity_armor_wing.png");
    this.wingGlowTex = new ResourceLocation(this.s + "infinity_armor_wingglow.png");
    this.mc = Minecraft.getInstance();
    this.buf = this.mc.renderBuffers().bufferSource();
    this.randy = new Random();
    this.invuln = new HumanoidModel(createMesh(new CubeDeformation(0.0F), 0.0F).getRoot().bake(64, 64));
  }

  public static MeshDefinition createMeshes(CubeDeformation с, float f, boolean islegs) {
    legs = islegs;
    int heightoffset = 0;
    int legoffset = islegs ? 32 : 0;
    MeshDefinition m = new MeshDefinition();
    PartDefinition p = m.getRoot();
    p.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, с), PartPose.offset(0.0F, 0.0F + f, 0.0F));
    p.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
    p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, с), PartPose.offset(0.0F, 0.0F + f, 0.0F));
    p.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(-5.0F, 2.0F + f, 0.0F));
    p.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(5.0F, 2.0F + f, 0.0F));
    p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(-1.9F, 12.0F + f, 0.0F));
    p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(1.9F, 12.0F + f, 0.0F));
    if (islegs) {
      p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16 + legoffset).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, (float)(0 + heightoffset), 0.0F));
      p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, (float)(12 + heightoffset), 0.0F));
      p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(1.9F, (float)(12 + heightoffset), 0.0F));
    }

    return m;
  }

  public LayerDefinition rebuildWings() {
    MeshDefinition m = new MeshDefinition();
    PartDefinition p = m.getRoot();
    p.addOrReplaceChild("bipedRightWing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 2.0F, 0.0F, 1.2566371F, 0.0F));
    p.addOrReplaceChild("bipedLeftWing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, 2.0F, 0.0F, -1.2566371F, 0.0F));
    return LayerDefinition.create(m, 64, 64);
  }

  void renderToBufferWing(PoseStack m, VertexConsumer v, int l, int o, float r, float g, float b, float a) {
    ModelPart h = this.rebuildWings().bakeRoot();
    this.bipedRightWing = h.getChild("bipedRightWing");
    this.bipedLeftWing = h.getChild("bipedLeftWing");
    this.bipedRightWing.render(m, v, l, o, r, g, b, a);
    this.bipedLeftWing.render(m, v, l, o, r, g, b, a);
  }

  public void renderToBuffer(PoseStack mStack, VertexConsumer vertexBuilder, int i1, int i2, float i3, float i4, float i5, float i6) {
    InfinityArmorModel model = new InfinityArmorModel(this.rebuildWings().bakeRoot(), 0);
    this.copyBipedAngles(this, this.invuln);
    super.renderToBuffer(mStack, vertexBuilder, i1, i2, i3, i4, i5, i6);
    long time = 0;
    if (this.mc.player != null) {
      time = this.mc.player.level.getGameTime();
    }
    double pulse = Math.sin((double)time / 10.0) * 0.5 + 0.5;
    double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
    float f;
    float f1;
    float f2;
    if (this.young) {
      f = 1.5F / this.babyHeadScale;
      f1 = 1.0F / this.babyBodyScale;
      f2 = 1.0F;
    } else {
      f = 1.0F;
      f1 = 1.0F;
      f2 = 0.0F;
    }

    AvaritiaShaders.cosmicOpacity.set(1.0F);
    MoreClientInit.cosmicOpacity2.set(0.2F);
    if (MoreClientInit.inventoryRender) {
      AvaritiaShaders.cosmicExternalScale.set(25.0F);
      MoreClientInit.cosmicExternalScale2.set(25.0F);
    } else {
      AvaritiaShaders.cosmicExternalScale.set(1.0F);
      MoreClientInit.cosmicExternalScale2.set(1.0F);
      AvaritiaShaders.cosmicYaw.set((float)((double)(this.mc.player.getYRot() * 2.0F) * Math.PI / 360.0));
      AvaritiaShaders.cosmicPitch.set(-((float)((double)(this.mc.player.getXRot() * 2.0F) * Math.PI / 360.0)));
      MoreClientInit.cosmicYaw2.set((float)((double)(this.mc.player.getYRot() * 2.0F) * Math.PI / 360.0));
      MoreClientInit.cosmicPitch2.set(-((float)((double)(this.mc.player.getXRot() * 2.0F) * Math.PI / 360.0)));
    }

    mStack.pushPose();
    mStack.scale(f, f, f);
    mStack.translate(0.0, (double)(this.babyYHeadOffset / 16.0F * f2), 0.0);
    this.head.render(mStack, mat(MoreClientInit.MASK).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6);
    if (invulnRender && !player) {
      this.hatsOver().forEach((t) -> {
        t.render(mStack, mat(MoreClientInit.MASK_INV).buffer(this.buf, InfinityArmorModel::mask2), i1, i2, i3, i4, i5, i6);
      });
    }

    mStack.popPose();
    mStack.pushPose();
    mStack.scale(f1, f1, f1);
    mStack.translate(0.0, (double)(this.bodyYOffset / 16.0F * f2), 0.0);
    if (invulnRender && !player) {
      this.bodyPartsOver().forEach((t) -> {
        t.render(mStack, mat(MoreClientInit.MASK_INV).buffer(this.buf, InfinityArmorModel::mask2), i1, i2, i3, i4, i5, i6);
      });
    }

    this.bodyParts().forEach((t) -> {
      t.render(mStack, mat(MoreClientInit.MASK).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6);
    });
    this.bodyParts().forEach((t) -> {
      t.render(mStack, this.ver(this.glow(this.eyeTex)), i1, i2, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5));
    });
    mStack.popPose();
    mStack.pushPose();
    this.randy.setSeed(time / 3L * 1723609L);
    float[] col = MathHelper.HSVtoRGB(this.randy.nextFloat() * 6.0F, 1.0F, 1.0F);
    mStack.scale(f, f, f);
    mStack.translate(0.0, (double)(this.babyYHeadOffset / 16.0F * f2), -0.029999999329447746);
    this.hat.render(mStack, mat(MoreClientInit.MASK).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6);
    if (invulnRender) {
      this.hat.render(mStack, this.ver(RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(this.eyeTex, false, false)).setCullState(RenderType.NO_CULL).createCompositeState(true))), i1, i2, col[0], col[1], col[2], 1.0F);
    }

    mStack.popPose();
    if (playerFlying && !MoreClientInit.inventoryRender) {
      mStack.pushPose();
      this.rebuildWings();
      mStack.scale(f1, f1, f1);
      mStack.translate(0.0, (this.bodyYOffset / 16.0F * f2), 0.0);
      model.renderToBufferWing(mStack, this.mc.renderBuffers().bufferSource().getBuffer(RenderType.armorCutoutNoCull(this.wingTex)), i1, i2, i3, i4, i5, i6);
      //model.renderToBufferWing(mStack, mat(EventHandle.WING).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6);
      model.renderToBufferWing(mStack, this.mc.renderBuffers().bufferSource().getBuffer(this.glow(this.wingGlowTex)), i1, i2, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5));
      mStack.popPose();
    }

  }

  public void update(LivingEntity e, ItemStack i, EquipmentSlot a) {
    invulnRender = false;
    playerFlying = false;
    player = false;
    ItemStack hats = e.getItemBySlot(EquipmentSlot.HEAD);
    ItemStack chest = e.getItemBySlot(EquipmentSlot.CHEST);
    ItemStack leg = e.getItemBySlot(EquipmentSlot.LEGS);
    ItemStack foot = e.getItemBySlot(EquipmentSlot.FEET);
    boolean hasHat = hats.getItem() == MoreAvaritiaModItems.SUPER_INFINITY_HEAD.get();
    boolean hasChest = chest.getItem() == MoreAvaritiaModItems.SUPER_INFINITY_CHEST.get();
    boolean hasLeg = leg.getItem() == MoreAvaritiaModItems.SUPER_INFINITY_LEG.get();
    boolean hasFoot = foot.getItem() == MoreAvaritiaModItems.SUPER_INFINITY_BOOTS.get();
    if (hasHat && hasChest && hasLeg && hasFoot) {
      invulnRender = true;
    }

    if (e instanceof Player) {
      player = true;
      if (hasChest && ((Player)e).getAbilities().flying) {
        playerFlying = true;
      }
    }

    this.crouching = e.isCrouching();
    this.young = e.isBaby();
    this.riding = e.isPassenger();
  }

  public VertexConsumer ver(RenderType t) {
    return this.buf.getBuffer(t);
  }

  public static Material mat(TextureAtlasSprite t) {
    return new Material(TextureAtlas.LOCATION_BLOCKS, t.getName());
  }

  public Iterable<ModelPart> bodyParts() {
    return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
  }

  public Iterable<ModelPart> hatsOver() {
    return ImmutableList.of(this.invuln.hat, this.invuln.head);
  }

  public Iterable<ModelPart> bodyPartsOver() {
    return ImmutableList.of(this.invuln.body, this.invuln.rightArm, this.invuln.leftArm, this.invuln.rightLeg, this.invuln.leftLeg);
  }

  void copyPartAngles(ModelPart s, ModelPart d) {
    d.xRot = s.xRot;
    d.yRot = s.yRot;
    d.zRot = s.zRot;
    d.x = s.x;
    d.y = s.y;
    d.z = s.z;
  }

  void copyBipedAngles(HumanoidModel s, HumanoidModel d) {
    this.copyPartAngles(s.head, d.head);
    this.copyPartAngles(s.hat, d.hat);
    this.copyPartAngles(s.body, d.body);
    this.copyPartAngles(s.leftArm, d.leftArm);
    this.copyPartAngles(s.leftLeg, d.leftLeg);
    this.copyPartAngles(s.rightArm, d.rightArm);
    this.copyPartAngles(s.rightLeg, d.rightLeg);
  }
}
