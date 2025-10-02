package net.wzz.more_avaritia.entity.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import committee.nova.mods.avaritia.api.client.render.CCRenderState;
import committee.nova.mods.avaritia.api.client.render.buffer.TransformingVertexConsumer;
import committee.nova.mods.avaritia.api.client.render.model.OBJParser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wzz.more_avaritia.colour.Colour;
import net.wzz.more_avaritia.colour.ColourRGBA;
import net.wzz.more_avaritia.entity.VoidEntity;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VoidRender extends EntityRenderer<VoidEntity> {
  private static final ResourceLocation VOID = new ResourceLocation("more_avaritia", "textures/entity/void.png");
  
  public VoidRender(EntityRendererProvider.Context context) {
    super(context);
  }
  
  @NotNull
  public ResourceLocation getTextureLocation(@NotNull VoidEntity p_114482_) {
    return VOID;
  }
  
  public void render(@NotNull VoidEntity ent, float entityYaw, float ticks, @NotNull PoseStack stack, @NotNull MultiBufferSource buf, int packedLightIn) {
    float age = ent.getAge() + ticks;
    Colour colour = getColour(age, 1.0D);
    double scale = VoidEntity.getVoidScale(age);
    double halocoord = 0.58D * scale;
    double haloscaledist = 2.2D * scale;
    Vec3 cam = this.entityRenderDispatcher.camera.getPosition();
    double dx = ent.getX() - cam.x();
    double dy = ent.getY() - cam.y();
    double dz = ent.getZ() - cam.z();
    stack.pushPose();
    stack.mulPose(Axis.YP.rotationDegrees((float)(Math.atan2(dx, dz) * 57.29577951308232D)));
    stack.mulPose(Axis.XP.rotationDegrees((float)(Math.atan2(Math.sqrt(dx * dx + dz * dz), dy) * 57.29577951308232D + 90.0D)));
    stack.pushPose();
    stack.mulPose(Axis.XP.rotationDegrees(90.0F));
    TransformingVertexConsumer cons = new TransformingVertexConsumer(buf.getBuffer((RenderType)RenderType.create("more_avaritia:void_halo", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(InfinityUtils.rl("textures/entity/void_halo.png"), false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_WRITE).createCompositeState(false))), stack);
    cons.vertex(-halocoord, 0.0D, -halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 0.0F).endVertex();
    cons.vertex(-halocoord, 0.0D, halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 1.0F).endVertex();
    cons.vertex(halocoord, 0.0D, halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 1.0F).endVertex();
    cons.vertex(halocoord, 0.0D, -halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 0.0F).endVertex();
    stack.popPose();
    stack.scale((float)scale, (float)scale, (float)scale);
    CCRenderState cc = CCRenderState.instance();
    cc.reset();
    cc.bind(RenderType.create("more_avaritia:void_hemisphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENTITY_SHADOW_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID, false, false)).setCullState(RenderType.NO_CULL).createCompositeState(false)), buf, stack);
    cc.baseColour = colour.rgba();
    ((new OBJParser(InfinityUtils.rl("models/hemisphere.obj"))).parse().get("model")).render(cc);
    stack.popPose();
  }
  
  public static Colour getColour(double age, double a) {
    double l = age / 1860.0D;
    double f = Math.max(0.0D, (l - 0.95D) / 0.050000000000000044D);
    f = Math.max(f, 1.0D - l * 30.0D);
    return new ColourRGBA(f, f, f, a);
  }
}
