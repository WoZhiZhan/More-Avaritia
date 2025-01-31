package net.wzz.more_avaritia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FakeInfinityArmorModel extends HumanoidModel {
  Minecraft mc;
  MultiBufferSource buf;

  public FakeInfinityArmorModel(ModelPart r) {
    super(r);
    this.mc = Minecraft.getInstance();
    this.buf = this.mc.renderBuffers().bufferSource();
  }

  public static MeshDefinition createMeshes(CubeDeformation с, float f, boolean islegs) {
    int heightoffset = 0;
    int legoffset = islegs ? 32 : 0;
    MeshDefinition m = new MeshDefinition();
    PartDefinition p = m.getRoot();
    p.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, с), PartPose.offset(0.0F, f, 0.0F));
    p.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
    p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, с), PartPose.offset(0.0F, f, 0.0F));
    p.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(-5.0F, 2.0F + f, 0.0F));
    p.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(5.0F, 2.0F + f, 0.0F));
    p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(-1.9F, 12.0F + f, 0.0F));
    p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, с), PartPose.offset(1.9F, 12.0F + f, 0.0F));
    if (islegs) {
      p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16 + legoffset).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, heightoffset, 0.0F));
      p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12 + heightoffset, 0.0F));
      p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(1.9F, 12 + heightoffset, 0.0F));
    }
    return m;
  }

  public void renderToBuffer(PoseStack mStack, VertexConsumer vertexBuilder, int i1, int i2, float i3, float i4, float i5, float i6) {
  }

  public void update(LivingEntity e, ItemStack i, EquipmentSlot a) {
    this.crouching = e.isCrouching();
    this.young = e.isBaby();
    this.riding = e.isPassenger();
  }
}

