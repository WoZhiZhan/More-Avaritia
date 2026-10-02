package net.wzz.more_avaritia.client.model;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.wzz.more_avaritia.client.comic.PerspectiveModel;
import net.wzz.more_avaritia.client.comic.PerspectiveModelState;
import net.wzz.more_avaritia.client.comic.TransformUtils;
import net.wzz.more_avaritia.init.ModShaders;
import org.joml.Matrix4f;

/**
 * 无尽能源：直接渲染的星空彩色光球，持续一大一小地膨胀
 */
public class InfinityEnergyModel implements PerspectiveModel {
    private static final int LATITUDE_SEGMENTS = 20;
    private static final int LONGITUDE_SEGMENTS = 32;
    private static final long ICON_START_NANOS = System.nanoTime();
    private static final float[] SPHERE = buildSphere(LATITUDE_SEGMENTS, LONGITUDE_SEGMENTS);

    private final BakedModel base;
    private final PerspectiveModelState state;

    public InfinityEnergyModel(BakedModel base) {
        this.base = base;
        this.state = (PerspectiveModelState) TransformUtils.stateFromItemTransforms(base.getTransforms());
    }

    @Override
    public PerspectiveModelState getModelState() {
        return state;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose, MultiBufferSource source, int packedLight, int packedOverlay) {
        boolean gui = ctx == ItemDisplayContext.GUI;
        var shader = gui ? ModShaders.INFINITY_ENERGY_GUI_SHADER : ModShaders.INFINITY_ENERGY_SHADER;
        if (shader == null) return;

        float time = ModShaders.renderTime + ModShaders.renderFrame;
        float iconSeconds = gui ? (float) ((System.nanoTime() - ICON_START_NANOS) * 1.0E-9D) : 0.0F;
        float pulse = gui ? 0.90F + 0.10F * Mth.sin(iconSeconds * 1.1F)
                : 0.85F + 0.15F * Mth.sin(time * 0.14F);
        float s = (gui ? 0.52F : 0.45F) * pulse;

        if (source instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endBatch();
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(gui);
        RenderSystem.disableCull();

        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.scale(s, s, s);
        Matrix4f mat = pose.last().pose();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, gui
                ? DefaultVertexFormat.POSITION_COLOR_NORMAL : DefaultVertexFormat.POSITION_COLOR_TEX);
        for (int index = 0; index < SPHERE.length; index += 5) {
            var vertex = builder.vertex(mat, SPHERE[index], SPHERE[index + 1], SPHERE[index + 2])
                    .color(1.0F, 1.0F, 1.0F, 1.0F);
            if (gui) {
                // Keep the untransformed sphere direction separate from GUI slot coordinates.
                vertex.normal(SPHERE[index], SPHERE[index + 1], SPHERE[index + 2]);
            } else {
                vertex.uv(SPHERE[index + 3], SPHERE[index + 4]);
            }
            vertex.endVertex();
        }
        if (gui && shader.getUniform("IconTime") != null) {
            shader.getUniform("IconTime").set(iconSeconds);
        }
        RenderSystem.setShader(() -> shader);
        BufferUploader.drawWithShader(builder.end());
        pose.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private static float[] buildSphere(int latitudeSegments, int longitudeSegments) {
        float[] vertices = new float[latitudeSegments * longitudeSegments * 4 * 5];
        int offset = 0;
        for (int latitude = 0; latitude < latitudeSegments; latitude++) {
            float latitude0 = (float) (-Math.PI * 0.5 + Math.PI * latitude / latitudeSegments);
            float latitude1 = (float) (-Math.PI * 0.5 + Math.PI * (latitude + 1) / latitudeSegments);
            float v0 = 1.0F - (float) latitude / latitudeSegments;
            float v1 = 1.0F - (float) (latitude + 1) / latitudeSegments;

            for (int longitude = 0; longitude < longitudeSegments; longitude++) {
                float angle0 = (float) (Math.PI * 2.0 * longitude / longitudeSegments);
                float angle1 = (float) (Math.PI * 2.0 * (longitude + 1) / longitudeSegments);
                float u0 = (float) longitude / longitudeSegments;
                float u1 = (float) (longitude + 1) / longitudeSegments;

                sphereVertex(vertices, offset, latitude0, angle0, u0, v0);
                sphereVertex(vertices, offset + 5, latitude1, angle0, u0, v1);
                sphereVertex(vertices, offset + 10, latitude1, angle1, u1, v1);
                sphereVertex(vertices, offset + 15, latitude0, angle1, u1, v0);
                offset += 20;
            }
        }
        return vertices;
    }

    private static void sphereVertex(float[] vertices, int offset,
                                     float latitude, float longitude, float u, float v) {
        float ring = (float) Math.cos(latitude);
        vertices[offset] = ring * (float) Math.cos(longitude);
        vertices[offset + 1] = (float) Math.sin(latitude);
        vertices[offset + 2] = ring * (float) Math.sin(longitude);
        vertices[offset + 3] = u;
        vertices[offset + 4] = v;
    }
}
