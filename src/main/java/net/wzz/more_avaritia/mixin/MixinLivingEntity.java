package net.wzz.more_avaritia.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Unique
    private final LivingEntity more_avaritia$livingEntity = (LivingEntity) (Object) this;
    @Inject(method = "getHealth", at = @At("HEAD"), cancellable = true)
    private void getHealth(CallbackInfoReturnable<Float> cir) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player))
            cir.setReturnValue(player.getMaxHealth());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            float health = player.getMaxHealth();
            if (health <= 0f) health = 20f;
            InfinityUtils.forceSetHealth(player, health, null);
        }
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void onSetHealth(float p_21154_, CallbackInfo ci) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            float health = player.getMaxHealth();
            if (health <= 0f) health = 20f;
            more_avaritia$livingEntity.entityData.set(LivingEntity.DATA_HEALTH_ID, health);
            ci.cancel();
        }
    }

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    public void knockback(double strength, double x, double z, CallbackInfo ci) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void push(Entity entity, CallbackInfo ci) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void isPushable(CallbackInfoReturnable<Boolean> cir) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public void die(DamageSource p_21014_, CallbackInfo ci) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "isDeadOrDying", at = @At("HEAD"), cancellable = true)
    public void isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
    public void isAlive(CallbackInfoReturnable<Boolean> cir) {
        if (more_avaritia$livingEntity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            cir.setReturnValue(true);
        }
    }
}
