package net.wzz.more_avaritia.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.entity.LivingEntity;
import committee.nova.mods.avaritia.util.ToolUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.fml.common.Mod;
import net.wzz.more_avaritia.item.InfinityArmorItem;
import net.wzz.more_avaritia.util.InfinityUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AbilityHandler
{
    public static final Set<String> entitiesWithHelmets;
    public static final Set<String> entitiesWithLeggings;
    public static final Set<String> entitiesWithBoots;
    public static final Map<String, AbilityHandler.FlightInfo> entitiesWithFlight;
    
    @SubscribeEvent
    public static void updateAbilities(final LivingEvent.LivingTickEvent event) {
        final LivingEntity entity = event.getEntity();
        if (entity instanceof final Player player) {
            final String key = player.getGameProfile().getName() + ":" + player.level().isClientSide;
            final boolean hasHelmet = ToolUtils.isPlayerWearing(event.getEntity(), EquipmentSlot.HEAD, item -> item instanceof InfinityArmorItem);
            final boolean hasChest = ToolUtils.isPlayerWearing(event.getEntity(), EquipmentSlot.CHEST, item -> item instanceof InfinityArmorItem);
            final boolean hasLeggings = ToolUtils.isPlayerWearing(event.getEntity(), EquipmentSlot.LEGS, item -> item instanceof InfinityArmorItem);
            final boolean hasBoots = ToolUtils.isPlayerWearing(event.getEntity(), EquipmentSlot.FEET, item -> item instanceof InfinityArmorItem);
            handleHelmetStateChange(player, key, hasHelmet);
            handleChestStateChange(player, key, hasChest);
            handleLeggingsStateChange(player, key, hasLeggings);
            handleBootsStateChange(player, key, hasBoots);
        }
    }
    
    private static void handleChestStateChange(final Player player, final String key, final boolean hasChest) {
        final boolean isFlyingGameMode = !InfinityUtils.isPlayingMode(player);
        final AbilityHandler.FlightInfo flightInfo = AbilityHandler.entitiesWithFlight.computeIfAbsent(key, uuid -> new AbilityHandler.FlightInfo());
        if (isFlyingGameMode || hasChest) {
            if (!flightInfo.hadFlightItem) {
                if (!player.getAbilities().mayfly) {
                    updateClientServerFlight(player, true);
                }
                flightInfo.hadFlightItem = true;
            }
            else if (flightInfo.wasFlyingGameMode && !isFlyingGameMode) {
                updateClientServerFlight(player, true, flightInfo.wasFlying);
            }
            else if (flightInfo.wasFlyingAllowed && !player.getAbilities().mayfly) {
                updateClientServerFlight(player, true, flightInfo.wasFlying);
            }
            flightInfo.wasFlyingGameMode = isFlyingGameMode;
            flightInfo.wasFlying = player.getAbilities().flying;
            flightInfo.wasFlyingAllowed = player.getAbilities().mayfly;
            if (hasChest) {
                final List<MobEffectInstance> effects = Lists.newArrayList((Iterable)player.getActiveEffects());
                for (final MobEffectInstance potion : Collections2.filter(effects, potion -> !potion.getEffect().isBeneficial())) {
                    player.removeEffect(potion.getEffect());
                }
            }
        }
        else {
            if (flightInfo.hadFlightItem) {
                if (player.getAbilities().mayfly) {
                    updateClientServerFlight(player, false);
                }
                flightInfo.hadFlightItem = false;
            }
            flightInfo.wasFlyingGameMode = false;
            flightInfo.wasFlying = player.getAbilities().flying;
            flightInfo.wasFlyingAllowed = player.getAbilities().mayfly;
        }
    }
    
    private static void handleHelmetStateChange(final Player player, final String key, final boolean hasHelmet) {
        if (hasHelmet) {
            if (AbilityHandler.entitiesWithHelmets.contains(key)) {
                player.setAirSupply(300);
                player.getFoodData().setFoodLevel(20);
                player.getFoodData().setSaturation(20.0f);
                MobEffectInstance nv = player.getEffect(MobEffects.NIGHT_VISION);
                if (nv == null) {
                    nv = new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false);
                    player.addEffect(nv);
                }
                nv.duration = 300;
            }
            else {
                AbilityHandler.entitiesWithHelmets.add(key);
            }
        }
        else {
            AbilityHandler.entitiesWithHelmets.remove(key);
        }
    }
    
    private static void handleLeggingsStateChange(final Player player, final String key, final boolean hasLeggings) {
        if (hasLeggings) {
            if (AbilityHandler.entitiesWithLeggings.contains(key)) {
                if (player.isOnFire()) {
                    player.clearFire();
                    player.fireImmune();
                }
            }
            else {
                AbilityHandler.entitiesWithLeggings.add(key);
            }
        }
        else {
            AbilityHandler.entitiesWithLeggings.remove(key);
        }
    }
    
    private static void handleBootsStateChange(final Player player, final String key, final boolean hasBoots) {
        if (hasBoots) {
            if (AbilityHandler.entitiesWithBoots.contains(key)) {
                player.setMaxUpStep(1.0625f);
                final boolean flying = player.getAbilities().flying;
                final boolean swimming = player.isInWater();
                final boolean sneaking = player.isCrouching();
                if (player.onGround() || flying || swimming) {
                    final float speed = 0.1f * (flying ? 1.1f : 1.0f) * (swimming ? 1.2f : 1.0f) * (sneaking ? 0.1f : 1.0f);
                    if (player.zza > 0.0f) {
                        player.moveRelative(speed, new Vec3(0.0, 0.0, 1.0));
                    }
                    else if (player.zza < 0.0f) {
                        player.moveRelative(-speed * 0.25f, new Vec3(0.0, 0.0, 1.0));
                    }
                    if (player.xxa != 0.0f) {
                        player.moveRelative(speed * 0.45f * Math.signum(player.xxa), new Vec3(1.0, 0.0, 0.0));
                    }
                }
                if (player.isSprinting()) {
                    final float f = player.getYRot() * 0.017453292f;
                    player.setDeltaMovement(player.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2f), 0.0, (double)(Mth.cos(f) * 0.2f)));
                }
            }
            else {
                AbilityHandler.entitiesWithBoots.add(key);
            }
        }
        else {
            player.setMaxUpStep(0.6f);
            AbilityHandler.entitiesWithBoots.remove(key);
        }
    }
    
    @SubscribeEvent
    public static void jumpBoost(final LivingEvent.LivingJumpEvent event) {
        final LivingEntity entity = event.getEntity();
        if (entity instanceof final Player player) {
            if (AbilityHandler.entitiesWithBoots.contains(player.getGameProfile().getName() + ":" + player.level().isClientSide) && player.isSprinting()) {
                player.setDeltaMovement(player.getDeltaMovement().add(0.0, 0.3050000071525574, 0.0));
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerDimensionChange(final PlayerEvent.PlayerChangedDimensionEvent event) {
        stripAbilities(event.getEntity());
        reapplyFly(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        stripAbilities(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        stripAbilities(event.getEntity());
        clearFly(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        stripAbilities(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onEntityDeath(final LivingDeathEvent event) {
        final LivingEntity entity2 = event.getEntity();
        if (entity2 instanceof final Player entity) {
            stripAbilities(entity);
        }
    }
    
    private static void stripAbilities(final Player player) {
        final String key = player.getGameProfile().getName() + ":" + player.level().isClientSide;
        AbilityHandler.entitiesWithHelmets.remove(key);
        AbilityHandler.entitiesWithFlight.remove(key);
        AbilityHandler.entitiesWithLeggings.remove(key);
        AbilityHandler.entitiesWithBoots.remove(key);
    }
    
    private static void clearFly(final Player player) {
        AbilityHandler.entitiesWithFlight.remove(player.getGameProfile().getName() + ":" + player.level().isClientSide);
    }
    
    private static void reapplyFly(final Player player) {
        final AbilityHandler.FlightInfo flightInfo = AbilityHandler.entitiesWithFlight.get(player.getGameProfile().getName() + ":" + player.level().isClientSide);
        if (flightInfo != null && (flightInfo.wasFlyingAllowed || flightInfo.wasFlying)) {
            updateClientServerFlight(player, flightInfo.wasFlyingAllowed, flightInfo.wasFlying);
        }
    }
    
    private static void updateClientServerFlight(final Player player, final boolean allowFlying) {
        updateClientServerFlight(player, allowFlying, allowFlying && player.getAbilities().flying);
    }
    
    private static void updateClientServerFlight(final Player player, final boolean allowFlying, final boolean isFlying) {
        player.getAbilities().mayfly = allowFlying;
        player.getAbilities().flying = isFlying;
        if (player instanceof final ServerPlayer serverPlayer) {
            serverPlayer.onUpdateAbilities();
        }
    }
    
    static {
        entitiesWithHelmets = new HashSet<String>();
        entitiesWithLeggings = new HashSet<String>();
        entitiesWithBoots = new HashSet<String>();
        entitiesWithFlight = new ConcurrentHashMap<String, AbilityHandler.FlightInfo>();
    }

    public static class FlightInfo {
    public boolean hadFlightItem;
    public boolean wasFlyingGameMode;
    public boolean wasFlyingAllowed;
    public boolean wasFlying;
}
}

