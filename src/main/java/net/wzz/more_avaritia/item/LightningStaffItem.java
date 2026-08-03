package net.wzz.more_avaritia.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wzz.more_avaritia.client.IItemType;
import net.wzz.more_avaritia.network.SkillBoltSpawnPacket;
import net.wzz.more_avaritia.network.util.NetworkHandler;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class LightningStaffItem extends Item implements IItemType {
    public LightningStaffItem() {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lightning_staff.text.1"));
        tooltip.add(Component.translatable("item.lightning_staff.text.2"));
        tooltip.add(Component.translatable("item.lightning_staff.text.3"));
        tooltip.add(Component.translatable("item.lightning_staff.text.4"));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }
        if (player.tickCount % 5 == 0 && !level.isClientSide) {
            Vec3 look = player.getLookAngle();
            Vec3 eye = player.getEyePosition(1.0f);
            Vec3 right = new Vec3(-look.z, 0, look.x).normalize();
            Vec3 startPoint = eye.add(look.scale(0.6)).add(right.scale(0.35));
            HitResult hit = player.pick(40.0, 1.0f, false);
            Vec3 targetPoint = hit.getType() == HitResult.Type.MISS
                    ? eye.add(look.scale(40.0))
                    : hit.getLocation();
            Random random = new Random();
            long seed = random.nextLong();
            SkillBoltSpawnPacket packet = new SkillBoltSpawnPacket(
                    startPoint, 
                    targetPoint, 
                    seed, 
                    10, 
                    0.13f,
                    0.60f, 
                    0.72f, 
                    1.0f
            );
            NetworkHandler.sendToTrackingEntityAndSelf(packet, player);
            AABB damageArea = new AABB(startPoint, targetPoint).inflate(2.0);
            List<Entity> entities = level.getEntities(player, damageArea, e -> e instanceof LivingEntity);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity2) {
                    if (!livingEntity2.hasLineOfSight(player)) continue;
                    float damage = 1000.0F;
                    InfinityUtils.forceSetHealth(livingEntity2, Math.max(livingEntity2.getHealth() - damage, 0f),
                            livingEntity2.damageSources().mobAttack(player), player);
                    if (!livingEntity2.isDeadOrDying())
                        livingEntity2.hurt(level.damageSources().lightningBolt(), damage);
                    if (livingEntity2.isOnFire()) {
                        livingEntity2.setRemainingFireTicks(livingEntity2.getRemainingFireTicks() + 20);
                    } else {
                        livingEntity2.setRemainingFireTicks(40);
                    }
                }
            }
            if (player.tickCount % 15 == 0) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                        SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 0.8F, 0.8F);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player && !level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }

    @Override
    public Type getItemType() {
        return Type.TOOL;
    }
}
