package net.wzz.more_avaritia.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow @Final private static EntityDataAccessor<Float> DATA_HEALTH_ID;

    @Shadow public abstract float getMaxHealth();

    private final LivingEntity livingEntity = (LivingEntity) (Object) this;
    @Inject(method = "getHealth", at = @At("HEAD"), cancellable = true)
    private void getHealth(CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player))
            cir.setReturnValue(player.getMaxHealth());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            InfinityUtils.forceSetHealth(player, player.getMaxHealth(), null);
        }
    }

    /**
     * @author wzz
     * @reason use
     */
    @Overwrite
    public void setHealth(float p_21154_) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player))
            livingEntity.entityData.set(DATA_HEALTH_ID, this.getMaxHealth());
        livingEntity.entityData.set(DATA_HEALTH_ID, Mth.clamp(p_21154_, 0.0F, this.getMaxHealth()));
    }

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    public void knockback(double strength, double x, double z, CallbackInfo ci) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void push(Entity entity, CallbackInfo ci) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void isPushable(CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public void die(DamageSource p_21014_, CallbackInfo ci) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "isDeadOrDying", at = @At("HEAD"), cancellable = true)
    public void isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(false);
        }
    }
}
