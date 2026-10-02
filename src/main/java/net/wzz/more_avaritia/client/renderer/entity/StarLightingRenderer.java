package net.wzz.more_avaritia.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LightningBolt;
import net.wzz.more_avaritia.init.ModRenderTypes;
import org.joml.Matrix4f;

/**
 * 星空之雷：完全沿用原版 MC 闪电的几何（竖直折线 + 4 层分支），
 * 只把渲染类型换成 star_lightning 着色器，由 shader 生成星云底色、星流与闪烁星点。
 */
public class StarLightingRenderer extends LightningBoltRenderer {
    public StarLightingRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(LightningBolt lightning, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        float[] xOffsets = new float[8];
        float[] zOffsets = new float[8];
        float x = 0.0F;
        float z = 0.0F;
        RandomSource random = RandomSource.create(lightning.seed);

        for (int i = 7; i >= 0; --i) {
            xOffsets[i] = x;
            zOffsets[i] = z;
            x += (float) (random.nextInt(11) - 5);
            z += (float) (random.nextInt(11) - 5);
        }

        VertexConsumer consumer = buffer.getBuffer(ModRenderTypes.STAR_LIGHTNING);
        Matrix4f matrix = pose.last().pose();

        for (int layer = 0; layer < 4; ++layer) {
            RandomSource layerRandom = RandomSource.create(lightning.seed);

            for (int branch = 0; branch < 3; ++branch) {
                int end = 7;
                int start = 0;
                if (branch > 0) {
                    end = 7 - branch;
                    start = end - 2;
                }

                float currentX = xOffsets[end] - x;
                float currentZ = zOffsets[end] - z;

                for (int segment = end; segment >= start; --segment) {
                    float prevX = currentX;
                    float prevZ = currentZ;

                    if (branch == 0) {
                        currentX += (float) (layerRandom.nextInt(11) - 5);
                        currentZ += (float) (layerRandom.nextInt(11) - 5);
                    } else {
                        currentX += (float) (layerRandom.nextInt(31) - 15);
                        currentZ += (float) (layerRandom.nextInt(31) - 15);
                    }

                    float width1 = 0.1F + (float) layer * 0.2F;
                    if (branch == 0) {
                        width1 *= (float) segment * 0.1F + 1.0F;
                    }

                    float width2 = 0.1F + (float) layer * 0.2F;
                    if (branch == 0) {
                        width2 *= ((float) segment - 1.0F) * 0.1F + 1.0F;
                    }

                    quad(matrix, consumer, currentX, currentZ, segment, prevX, prevZ, width1, width2, false, false, true, false);
                    quad(matrix, consumer, currentX, currentZ, segment, prevX, prevZ, width1, width2, true, false, true, true);
                    quad(matrix, consumer, currentX, currentZ, segment, prevX, prevZ, width1, width2, true, true, false, true);
                    quad(matrix, consumer, currentX, currentZ, segment, prevX, prevZ, width1, width2, false, true, false, false);
                }
            }
        }
    }

    private static void quad(Matrix4f matrix, VertexConsumer consumer,
                             float x1, float z1, int y, float x2, float z2,
                             float width1, float width2,
                             boolean flipX1, boolean flipZ1, boolean flipX2, boolean flipZ2) {
        consumer.vertex(matrix, x1 + (flipX1 ? width2 : -width2), (float) (y * 16), z1 + (flipZ1 ? width2 : -width2)).color(0.45F, 0.45F, 0.5F, 1.0F).endVertex();
        consumer.vertex(matrix, x2 + (flipX1 ? width1 : -width1), (float) ((y + 1) * 16), z2 + (flipZ1 ? width1 : -width1)).color(0.45F, 0.45F, 0.5F, 1.0F).endVertex();
        consumer.vertex(matrix, x2 + (flipX2 ? width1 : -width1), (float) ((y + 1) * 16), z2 + (flipZ2 ? width1 : -width1)).color(0.45F, 0.45F, 0.5F, 1.0F).endVertex();
        consumer.vertex(matrix, x1 + (flipX2 ? width2 : -width2), (float) (y * 16), z1 + (flipZ2 ? width2 : -width2)).color(0.45F, 0.45F, 0.5F, 1.0F).endVertex();
    }
}
