package net.wzz.more_avaritia.util;

import committee.nova.mods.avaritia.common.item.tools.InfinityArmorItem;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ForgeHooks;
import net.wzz.more_avaritia.init.ModDamageTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.ChunkEntities;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.wzz.more_avaritia.entity.StarLightingEntity;
import net.wzz.more_avaritia.init.MoreAvaritiaModEntities;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.network.ClientEntityRemovePacket;
import net.wzz.more_avaritia.network.util.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class InfinityUtils {
    public static boolean isInfinite(LivingEntity player) {
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof InfinityArmorItem)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void aoeAttack(Player player, float range, float damage, boolean hurtAnimal, boolean lightOn, boolean ms) {
        if (!player.level().isClientSide) {
            AABB aabb = player.getBoundingBox().deflate(range);
            List<Entity> toAttack = player.level().getEntities(player, aabb);
            DamageSource src = player.damageSources().source(ModDamageTypes.INFINITY, player, player);
            toAttack.stream().filter((entity) -> entity instanceof Mob).forEach((entity) -> {
                Mob mob = (Mob) entity;
                StarLightingEntity lightningbolt = new StarLightingEntity(MoreAvaritiaModEntities.STAR_LIGHTING.get(), player.level());
                if (lightOn) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
                    player.level().addFreshEntity(lightningbolt);
                }
                label38:
                {
                    if (mob instanceof Animal animal) {
                        if (hurtAnimal) {
                            animal.hurt(src, damage);
                            if (noDie(animal))
                                superAttack(animal);
                            break label38;
                        }
                    }
                    if (!(mob instanceof Animal)) {
                        if (ms) {
                            killEntity(mob, player);
                        } else {
                            mob.setHealth(0.0F);
                            mob.die(player.damageSources().source(ModDamageTypes.INFINITY, player, mob));
                            if (noDie(mob))
                                superAttack(mob);
                        }
                    }
                }
            });
        }
    }

    public static void superAttack(LivingEntity living) {
        if (!noDie(living))
            return;
        living.setHealth(0);
        living.removalReason = Entity.RemovalReason.DISCARDED;
        living.discard();
        living.onRemovedFromWorld();
        living.setRemoved(Entity.RemovalReason.DISCARDED);
        living.onClientRemoval();
        living.levelCallback.onRemove(Entity.RemovalReason.DISCARDED);
        living.setPos(-999,-999,-999);
        living.setInvisible(true);
        living.remove(Entity.RemovalReason.DISCARDED);
        ListUtils.addDieEntity(living);
    }

    public static void easyAttack(LivingEntity living, Player player) {
        if (!noDie(living))
            return;
        killEntity(living, player);
    }

    public static boolean noDie(LivingEntity living) {
        if (living == null)
            return false;
        if (living.getHealth() > 0)
            return true;
        return living.deathTime < 1;
    }

    public static void sweepAttack(Level level, LivingEntity livingEntity, LivingEntity victim) {
        if (livingEntity instanceof Player player) {
            Iterator<LivingEntity> var4 = level.getEntitiesOfClass(LivingEntity.class, player.getItemInHand(InteractionHand.MAIN_HAND).getSweepHitBox(player, victim)).iterator();
            while(true) {
                LivingEntity livingentity;
                double entityReachSq;
                do {
                    do {
                        if (!var4.hasNext()) {
                            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, livingEntity.getSoundSource(), 1.0F, 1.0F);
                            double d0 = (-Mth.sin(player.getYRot() * 0.017453292F));
                            entityReachSq = Mth.cos(player.getYRot() * 0.017453292F);
                            if (level instanceof ServerLevel serverLevel) {
                                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5), player.getZ() + entityReachSq, 0, d0, 0.0, entityReachSq, 0.0);
                            }
                            return;
                        }
                        livingentity = var4.next();
                        entityReachSq = Mth.square(player.getEntityReach());
                    } while(player.isAlliedTo(livingentity));
                } while(livingentity instanceof ArmorStand && ((ArmorStand)livingentity).isMarker());
                if (player.distanceToSqr(livingentity) < entityReachSq) {
                    livingentity.knockback(0.6000000238418579, (double)Mth.sin(player.getYRot() * 0.017453292F), (double)(-Mth.cos(player.getYRot() * 0.017453292F)));
                    victim.setHealth(0.0F);
                    victim.die(player.damageSources().source(ModDamageTypes.INFINITY, player, victim));
                }
            }
        }
    }

    public static void sweepAttackNormal(Level level, LivingEntity livingEntity, LivingEntity victim, int fors) {
        if (livingEntity instanceof Player player) {
            Iterator<LivingEntity> var4 = level.getEntitiesOfClass(LivingEntity.class, player.getItemInHand(InteractionHand.MAIN_HAND).getSweepHitBox(player, victim)).iterator();
            while(true) {
                LivingEntity livingentity;
                double entityReachSq;
                do {
                    do {
                        if (!var4.hasNext()) {
                            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, livingEntity.getSoundSource(), 1.0F, 1.0F);
                            double d0 = (-Mth.sin(player.getYRot() * 0.017453292F));
                            entityReachSq = Mth.cos(player.getYRot() * 0.017453292F);
                            if (level instanceof ServerLevel serverLevel) {
                                for (int i = 0; i < fors; i++) {
                                    double offset = (Math.random() - 1.5) * 1.5;
                                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5), player.getZ() + entityReachSq + offset, 0, d0, 0.0, entityReachSq, 0.0);
                                }
                            }
                            return;
                        }
                        livingentity = var4.next();
                    } while(player.isAlliedTo(livingentity));
                } while(livingentity instanceof ArmorStand && ((ArmorStand)livingentity).isMarker());
            }
        }
    }

    public static boolean hasItem(Player player, Item i) {
        if (player == null)
            return false;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == i) {
                return true;
            }
        }
        return player.getInventory().contains(new ItemStack(i));
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("more_avaritia", path);
    }

    public static boolean hasFakeInfinityArmor(Player player) {
        if (player == null)
            return false;
        if (player.getInventory() == null || player.getInventory().armor == null)
            return false;
        return player.getInventory().getArmor(3).getItem() == MoreAvaritiaModItems.INFINITY_FAKE_HELMET.get() &&
                player.getInventory().getArmor(2).getItem() == MoreAvaritiaModItems.INFINITY_FAKE_CHESTPLATE.get() &&
                player.getInventory().getArmor(1).getItem() == MoreAvaritiaModItems.INFINITY_FAKE_LEGGINGS.get() &&
                player.getInventory().getArmor(0).getItem() == MoreAvaritiaModItems.INFINITY_FAKE_BOOTS.get();
    }

    public static boolean hasInfinityArmor(Player player) {
        if (player == null)
            return false;
        if (player.getInventory() == null || player.getInventory().armor == null)
            return false;
        return player.getInventory().getArmor(3).getItem() == MoreAvaritiaModItems.INFINITY_HEAD.get() &&
                player.getInventory().getArmor(2).getItem() == MoreAvaritiaModItems.INFINITY_Chestplate.get() &&
                player.getInventory().getArmor(1).getItem() == MoreAvaritiaModItems.INFINITY_LEGS.get() &&
                player.getInventory().getArmor(0).getItem() == MoreAvaritiaModItems.INFINITY_BOOTS.get();
    }

    public static void playSound(LevelAccessor world, SoundEvent s, LivingEntity living) {
        if (world instanceof Level _level) {
            if (!_level.isClientSide()) {
                _level.playSound(null, living, s, SoundSource.NEUTRAL, 1, 1);
            } else {
                _level.playLocalSound(living.getX(), living.getY(), living.getZ(), s, SoundSource.NEUTRAL, 1, 1, false);
            }
        }
    }

    public static boolean isPlayingMode(Player player) {
        return !player.isCreative() && !player.isSpectator();
    }

    public static void killEntity(Entity entity, LivingEntity source) {
        if (!(entity instanceof LivingEntity living)) {
            if (entity instanceof PartEntity<?> part) {
                killEntity(part.getParent(), source);
                part.discard();
            }
            return;
        }
        if (living instanceof EnderDragon dragon) {
            if (!dragon.isDeadOrDying() && dragon.getClass() == EnderDragon.class) {
                dragon.setHealth(0.0F);
                dragon.getPhaseManager().setPhase(EnderDragonPhase.DYING);
                return;
            }
        }
        spawnParticles(living, ParticleTypes.POOF, 10, 0.02d);
        if (!living.level.isClientSide) {
            if (source != null) {
                source.killedEntity((ServerLevel) source.level, living);
                source.awardKillScore(living, 1, living.level.damageSources().mobAttack(source));
                forceDropLoot(living, living.level.damageSources().mobAttack(source), source);
            }
            else
                forceDropLoot(living, living.level.damageSources().fellOutOfWorld(), source);
        }
        if (living.getClass() == EnderDragon.class) {
            if (source != null) {
                living.hurt(living.level.damageSources().mobAttack(source), Float.POSITIVE_INFINITY);
            } else living.hurt(living.level.damageSources().fellOutOfWorld(), Float.POSITIVE_INFINITY);
            return;
        }
        if (living.level instanceof ServerLevel serverLevel) {
            CustomBossEvents bossEvents = serverLevel.getServer().getCustomBossEvents();
            for (CustomBossEvent event : bossEvents.getEvents()) {
                event.removeAllPlayers();
                event.setVisible(false);
            }
            serverLevel.getChunkSource().removeEntity(living);
            serverLevel.entityManager.visibleEntityStorage.remove(living);
            serverLevel.entityManager.knownUuids.remove(living.getUUID());
            serverLevel.entityTickList.remove(living);
            if (living instanceof Mob mob) {
                serverLevel.navigatingMobs.remove(mob);
            }
            serverLevel.entityManager.callbacks.onDestroyed(living);
            serverLevel.entityManager.visibleEntityStorage.byUuid.remove(living.getUUID());
            serverLevel.entityManager.visibleEntityStorage.byId.remove(living.getId());
            long i = SectionPos.asLong(living.blockPosition());
            EntitySection<Entity> entitysection = serverLevel.entityManager.sectionStorage.getOrCreateSection(i);
            entitysection.remove(living);
            living.levelCallback = new EntityInLevelCallback() {
                @Override
                public void onMove() {}

                @Override
                public void onRemove(Entity.@NotNull RemovalReason removalReason) {
                    if (!entitysection.remove(living)) {
                        serverLevel.entityManager.stopTicking(living);
                        serverLevel.entityManager.stopTracking(living);
                        serverLevel.entityManager.callbacks.onDestroyed(living);
                        serverLevel.entityManager.knownUuids.remove(living.getUUID());
                    }
                }
            };
            ChunkEntities<?> chunkentities;
            while((chunkentities = serverLevel.entityManager.loadingInbox.poll()) != null) {
                ChunkEntities<?> finalChunkentities = chunkentities;
                chunkentities.getEntities().forEach((p_157593_) -> {
                    if (p_157593_ instanceof Entity e) {
                        e.discard();
                        serverLevel.entityManager.loadingInbox.remove(finalChunkentities);
                    }
                });
            }
            serverLevel.getScoreboard().entityRemoved(living);
            living.gameEvent(GameEvent.ENTITY_DIE);
            EntitySection section = serverLevel.entityManager.sectionStorage.getSection(SectionPos.asLong(living.blockPosition()));
            if (section != null) {
                remove(section, (living));
                serverLevel.entityManager.removeSectionIfEmpty(SectionPos.asLong(living.blockPosition()), section);
            }
            NetworkHandler.sendToAllPlayers(new ClientEntityRemovePacket(living.getId()));
        }
        if (living.level instanceof ClientLevel clientLevel) {
            clientLevel.tickingEntities.remove(living);
            if (living.isMultipartEntity())
                for (PartEntity<?> part : living.getParts())
                    if (part != null)
                        clientLevel.partEntities.remove(part.getId());
            clientLevel.entityStorage.entityStorage.remove(living);
            clientLevel.entityStorage.entityStorage.byUuid.remove(living.getUUID());
            clientLevel.entityStorage.entityStorage.byId.remove(living.getId());
            EntitySection section = clientLevel.entityStorage.sectionStorage.getSection(SectionPos.asLong(living.blockPosition()));
            if (section != null) {
                remove(section, (living));
                clientLevel.entityStorage.removeSectionIfEmpty(SectionPos.asLong(living.blockPosition()), section);
            }
        }
        living.dead = true;
        living.removalReason = Entity.RemovalReason.DISCARDED;
        living.setDiscardFriction(true);
        living.hasCustomOutlineRendering(null);
        living.setTicksFrozen(999999);
        living.setAirSupply(0);
        living.teleportTo(999999,999999,999999);
        living.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK);
        living.setRemoved(Entity.RemovalReason.KILLED);
        living.setRemoved(Entity.RemovalReason.DISCARDED);
        living.wasInPowderSnow = false;
        living.onRemovedFromWorld();
        living.setPose(Pose.DYING);
        living.getActiveEffects().clear();
        living.discard();
        living.invalidateCaps();
    }

    public static void forceDropLoot(LivingEntity living, @Nullable DamageSource source, LivingEntity attacker) {
        if (living == null || !(living.level instanceof ServerLevel serverLevel))
            return;
        if (living instanceof Player) return;
        Entity sourceEntity = source != null ? source.getDirectEntity() : attacker;
        if (attacker != null && sourceEntity != attacker)
            sourceEntity = attacker;
        if (source == null) {
            if (attacker != null) {
                source = living.damageSources().source(ModDamageTypes.INFINITY, attacker, living);
            } else {
                source = living.damageSources().generic();
            }
        }
        int lootingLevel = ForgeHooks.getLootingLevel(living, sourceEntity, source);
        living.captureDrops(new ArrayList<>());
        boolean killedByPlayer = sourceEntity instanceof Player;
        if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            ResourceLocation lootTableId = living.getLootTable();
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableId);
            if (lootTable != LootTable.EMPTY) {
                LootParams.Builder builder = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.THIS_ENTITY, living)
                        .withParameter(LootContextParams.ORIGIN, living.position())
                        .withParameter(LootContextParams.DAMAGE_SOURCE, source);
                if (sourceEntity instanceof LivingEntity killer) {
                    builder.withParameter(LootContextParams.KILLER_ENTITY, killer);
                    builder.withParameter(LootContextParams.DIRECT_KILLER_ENTITY, killer);
                    if (killer instanceof Player player) {
                        builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
                        builder.withLuck(player.getLuck() + lootingLevel);
                    }
                }
                LootParams params = builder.create(LootContextParamSets.ENTITY);
                List<ItemStack> drops = lootTable.getRandomItems(params);
                for (ItemStack stack : drops) {
                    if (!stack.isEmpty()) {
                        living.spawnAtLocation(stack, 0.0F);
                    }
                }
            }
            living.dropCustomDeathLoot(source, lootingLevel, killedByPlayer);
        }
        living.dropEquipment();
        if (living.shouldDropExperience()) {
            ExperienceOrb.award(serverLevel, living.position(), living.getExperienceReward());
        }
        Collection<ItemEntity> capturedDrops = living.captureDrops(null);
        if (!ForgeHooks.onLivingDrops(living, source, capturedDrops, lootingLevel, killedByPlayer)) {
            forEachServerLevelAddFreshEntity(capturedDrops, serverLevel);
        }
        living.dropAllDeathLoot(source);
    }

    public static void forEachServerLevelAddFreshEntity(
            Collection<? extends Entity> collection,
            net.minecraft.server.level.ServerLevel serverLevel
    ) {
        if (collection == null || serverLevel == null) return;
        for (Entity entity : collection) {
            serverLevel.addFreshEntity(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean remove(EntitySection section, Entity entity) {
        return removeObject(section.storage, entity);
    }

    public static boolean removeObject(ClassInstanceMultiMap<Entity> multiMap, Entity entity) {
        boolean removed = false;

        for (Map.Entry<Class<?>, List<Entity>> entry : multiMap.byClass.entrySet()) {
            if (entry.getKey().isInstance(entity)) {
                List<Entity> entityList = entry.getValue();
                removed |= entityList.remove(entity);
            }
        }
        return removed;
    }

    public static void spawnParticles(Entity living, SimpleParticleType particleType, int number, double speed) {
        if (living.level instanceof ServerLevel serverLevel) {
            for (int k = 0; k < number; ++k) {
                Random rand = new Random();
                serverLevel.sendParticles((ParticleOptions)particleType,
                        living.getX() + (double) (rand.nextFloat() * living.getBbWidth() * 2.0F) - (double) living.getBbWidth(),
                        living.getY() + (double) (rand.nextFloat() * living.getBbHeight()),
                        living.getZ() + (double) (rand.nextFloat() * living.getBbWidth() * 2.0F) - (double) living.getBbWidth(),
                        1,0,0, 0, speed);
            }
        }
    }

    public static void forceSetHealth(LivingEntity living, float f, DamageSource damageSource) {
        if (living == null)
            return;
        if (f > 0f) {
            living.hurtTime = 0;
        } else {
            living.hurtTime = 20;
        }
        SynchedEntityData newData = living.entityData;
        newData.set(LivingEntity.DATA_HEALTH_ID, f);
        living.entityData = newData;
        if (f <= 0f && !living.level.isClientSide && living.getServer() != null && damageSource != null) {
            if (living instanceof Player player) {
                if (!living.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                    player.inventory.dropAll();
                }
            } else living.dropAllDeathLoot(damageSource);
        }
    }
}
