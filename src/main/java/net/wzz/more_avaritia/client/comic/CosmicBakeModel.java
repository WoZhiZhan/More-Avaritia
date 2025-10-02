package net.wzz.more_avaritia.client.comic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.wzz.more_avaritia.init.ModRenderType;
import net.wzz.more_avaritia.init.ModShaders;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CosmicBakeModel extends WrappedItemModel {
    public static final float[] COSMIC_UVS = new float[40];
    private final List<ResourceLocation> maskSprite;

    public CosmicBakeModel(final BakedModel wrapped, final List<ResourceLocation> maskSprite) {
        super(wrapped);
        this.maskSprite = maskSprite;
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource source, int light, int overlay) {
        if (stack.getItem().getClass().getName().startsWith("net.wzz.more_avaritia.item.tools")) {
            this.parentState = TransformUtils.DEFAULT_TOOL;
        } else if (stack.getItem().getClass().getName().startsWith("net.wzz.more_avaritia.item.bow")) {
            this.parentState = TransformUtils.DEFAULT_BOW;
        } else this.parentState = TransformUtils.DEFAULT_ITEM;
        this.renderWrapped(stack, pStack, source, light, overlay, true);
        if (source instanceof MultiBufferSource.BufferSource bs) {
            bs.endBatch();
        }
        final Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0f;
        float pitch = 0.0f;
        float scale = 1f;
        if (transformType == ItemDisplayContext.GUI) {
            scale = 100.0F;
        } else {
            yaw = (float) (mc.player.getYRot() * 2.0f * Math.PI / 360.0);
            pitch = -(float) (mc.player.getXRot() * 2.0f * Math.PI / 360.0);
        }

        ModShaders.cosmicTime
                .set((System.currentTimeMillis() - ModShaders.renderTime) / 2000.0F);
        ModShaders.cosmicYaw.set(yaw);
        ModShaders.cosmicPitch.set(pitch);
        ModShaders.cosmicExternalScale.set(scale);
        ModShaders.cosmicOpacity.set(1.0F);
        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = Minecraft.getInstance()
                    .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(InfinityUtils.rl("misc/cosmic_" + i));
            COSMIC_UVS[i * 4] = sprite.getU0();
            COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        if (ModShaders.cosmicUVs != null) {
            ModShaders.cosmicUVs.set(COSMIC_UVS);
        }
        final VertexConsumer cons = source.getBuffer(ModRenderType.COSMIC);
        List<TextureAtlasSprite> atlasSprite = new ArrayList<>();
        for (ResourceLocation res : maskSprite) {
            atlasSprite.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
        }
        mc.getItemRenderer().renderQuadList(pStack, cons, bakeItem(atlasSprite), stack, light, overlay);
    }

    @Override
    public @Nullable PerspectiveModelState getModelState() {
        return (PerspectiveModelState) this.parentState;
    }

    @Override
    public boolean isCosmic() {
        return true;
    }
}
