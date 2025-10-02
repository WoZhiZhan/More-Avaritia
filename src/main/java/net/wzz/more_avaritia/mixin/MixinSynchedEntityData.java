//package net.wzz.more_avaritia.mixin;
//
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.wzz.more_avaritia.util.InfinityUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(SynchedEntityData.class)
//public abstract class MixinSynchedEntityData {
//
//    @Shadow @Final private Entity entity;
//
//    @Shadow protected abstract <T> SynchedEntityData.DataItem<T> getItem(EntityDataAccessor<T> p_135380_);
//
//    @Shadow private boolean isDirty;
//
//    @SuppressWarnings("unchecked")
//    @Inject(method = "set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V", at = @At("HEAD"), cancellable = true)
//    private <T> void onSet(EntityDataAccessor<T> key, T value, boolean sync, CallbackInfo ci) {
//        if (key.equals(LivingEntity.DATA_HEALTH_ID)) {
//            if (entity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
//                ci.cancel();
//                SynchedEntityData.DataItem<T> dataitem = this.getItem(key);
//                if (sync || ObjectUtils.notEqual(player.getMaxHealth(), dataitem.getValue())) {
//                    dataitem.setValue((T)(Float)player.getMaxHealth());
//                    this.entity.onSyncedDataUpdated(key);
//                    dataitem.setDirty(true);
//                    this.isDirty = true;
//                }
//            }
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
//    private <T> void onGet(EntityDataAccessor<T> key, CallbackInfoReturnable<T> cir) {
//        if (key.equals(LivingEntity.DATA_HEALTH_ID)) {
//            Object entity = this.entity;
//            if (entity instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
//                cir.setReturnValue((T)(Float)(player.getMaxHealth()));
//            }
//        }
//    }
//}
