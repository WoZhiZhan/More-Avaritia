package net.wzz.more_avaritia.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.wzz.more_avaritia.init.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 最终幻想剑第一人称格挡姿势（参考 final_sword_star 的实现）。
 */
@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer {

    @Shadow
    protected abstract void applyItemArmTransform(PoseStack matrices, HumanoidArm arm, float equippedProgress);

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack matrices, MultiBufferSource buffer, int light);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void more_avaritia$renderGodSwordBlock(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand,
                                                   float swingProgress, ItemStack stack, float equippedProgress,
                                                   PoseStack matrices, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (player.isUsingItem() && stack.getUseAnimation() == UseAnim.BLOCK
                && stack.getItem() == ModItems.INFINITY_GOD_SWORD.get()) {
            boolean left = hand == InteractionHand.OFF_HAND;
            HumanoidArm arm = left ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
            matrices.pushPose();
            this.applyItemArmTransform(matrices, arm, equippedProgress);
            int hor = arm == HumanoidArm.RIGHT ? 1 : -1;
            matrices.translate(hor * -0.14F, 0.08F, 0.14F);
            matrices.mulPose(Axis.XP.rotationDegrees(-102.25F));
            matrices.mulPose(Axis.YP.rotationDegrees(hor * 13.365F));
            matrices.mulPose(Axis.ZP.rotationDegrees(hor * 78.05F));
            this.renderItem(player, stack, left ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, left, matrices, buffer, combinedLight);
            matrices.popPose();
            ci.cancel();
        }
    }
}
