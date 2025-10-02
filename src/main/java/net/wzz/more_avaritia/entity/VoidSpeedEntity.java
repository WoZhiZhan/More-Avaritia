package net.wzz.more_avaritia.entity;

import com.google.common.base.Predicate;
import committee.nova.mods.avaritia.common.entity.ImmortalItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.wzz.more_avaritia.init.MoreAvaritiaModEntities;

import java.util.List;

public class VoidSpeedEntity extends Entity {
  public static final EntityDataAccessor<Integer> AGE_PARAMETER = SynchedEntityData.defineId(VoidSpeedEntity.class, EntityDataSerializers.INT);

  public static final Predicate<Entity> SUCK_PREDICATE;

  public static final Predicate<Entity> OMNOM_PREDICATE;

  static {
    SUCK_PREDICATE = (input -> {
        if (input instanceof Player) {
          Player p = (Player)input;
          return (!p.isCreative() || !p.isFallFlying());
        }
        return true;
      });
    OMNOM_PREDICATE = (input -> {
        if (!(input instanceof LivingEntity))
          return false;
        if (input instanceof Player) {
          Player p = (Player)input;
          return !p.isCreative();
        }
        return !(input instanceof ImmortalItemEntity);
      });
  }

  public static double collapse = 0.95D;

  public static double suckRange = 20.0D;


  private LivingEntity user;

  public VoidSpeedEntity(EntityType<VoidSpeedEntity> p_19870_, Level p_19871_) {
    super(p_19870_, p_19871_);
    this.noCulling = true;
  }

  public VoidSpeedEntity(PlayMessages.SpawnEntity packet, Level world) {
    this(MoreAvaritiaModEntities.VOID_SPEED.get(), world);
    this.noCulling = true;
  }

  public VoidSpeedEntity(Level level) {
    this(MoreAvaritiaModEntities.VOID_SPEED.get(), level);
  }

  public VoidSpeedEntity(Level level, LivingEntity shooter) {
    this(MoreAvaritiaModEntities.VOID_SPEED.get(), level);
    setUser(shooter);
  }

  public void setUser(LivingEntity user) {
    this.user = user;
  }

  public static double getVoidScale(double age) {
    return age * 2;
  }
  
  public int getAge() {
    return ((Integer)this.entityData.get(AGE_PARAMETER)).intValue();
  }
  
  private void setAge(int age) {
    this.entityData.set(AGE_PARAMETER, Integer.valueOf(age));
  }
  
  protected void defineSynchedData() {
    this.entityData.define(AGE_PARAMETER, Integer.valueOf(0));
  }
  
  protected void readAdditionalSaveData(CompoundTag tag) {
    setAge(tag.getInt("age"));
  }
  
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.putInt("age", getAge());
  }
  
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
  
  public void tick() {
    double posX = getX();
    double posY = getY();
    double posZ = getZ();
    BlockPos position = getOnPos();
    int age = getAge();
    if (age >= 40 && !(level()).isClientSide) {
      int j = 4;
      AABB aABB = new AABB(position.offset(-j, -j, -j), position.offset(j, j, j));
      List<Entity> list = level().getEntitiesOfClass(Entity.class, aABB, OMNOM_PREDICATE);
      list.stream()
        .filter(entity -> (entity != this))
        .forEach(entity -> {
            if (entity instanceof EnderDragon) {
              EnderDragon dragon = (EnderDragon)entity;
              dragon.hurt(dragon.head, level().damageSources().mobAttack(this.user), 100.0F);
            } else if (entity instanceof WitherBoss) {
              WitherBoss wither = (WitherBoss)entity;
              wither.setInvulnerableTicks(0);
              wither.hurt(level().damageSources().mobAttack(this.user), 100.0F);
            } else if (!(entity instanceof Player) && entity instanceof LivingEntity) {
              entity.hurt(level().damageSources().mobAttack(this.user), 100.0F);
            } 
          });
      remove(RemovalReason.KILLED);
    } else {
      setAge(age + 4);
    } 
    if ((level()).isClientSide)
      return;
    double size = getVoidScale(age) * 5D - 0.2D;
    int range = (int)(size * suckRange);
    AABB axisAlignedBB = new AABB(position.offset(-range, -range, -range), position.offset(range, range, range));
    List<Entity> sucked = level().getEntitiesOfClass(Entity.class, axisAlignedBB, (Predicate)SUCK_PREDICATE);
    double radius = getVoidScale(age) * 5D;
    for (Entity suckee : sucked) {
      if (suckee != this && !(suckee instanceof VoidThrowEntity) && !(suckee instanceof Player) && !(suckee instanceof ItemEntity)) {
        double dx = posX - suckee.getX();
        double dy = posY - suckee.getY();
        double dz = posZ - suckee.getZ();
        double lensquared = dx * dx + dy * dy + dz * dz;
        double len = Math.sqrt(lensquared);
        double lenn = len / suckRange;
        if (len <= suckRange) {
          double strength = (1.0D - lenn) * (1.0D - lenn);
          double power = 0.75D * radius;
          Vec3 motion = suckee.getDeltaMovement();
          double motionX = motion.x + dx / len * strength * power;
          double motionY = motion.y + dy / len * strength * power;
          double motionZ = motion.z + dz / len * strength * power;
          suckee.setDeltaMovement(motionX, motionY, motionZ);
          suckee.hurt(level().damageSources().mobAttack(this.user), 100f);
        } 
      }
    }
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public boolean shouldRenderAtSqrDistance(double p_19883_) {
    return true;
  }
}
