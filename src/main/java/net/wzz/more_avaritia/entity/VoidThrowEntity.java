package net.wzz.more_avaritia.entity;

import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.wzz.more_avaritia.init.MoreAvaritiaModEntities;
import org.jetbrains.annotations.NotNull;

public class VoidThrowEntity extends ThrowableItemProjectile {
    private LivingEntity shooter;

    public VoidThrowEntity(EntityType<VoidThrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public VoidThrowEntity(PlayMessages.SpawnEntity packet, Level level) {
        super(MoreAvaritiaModEntities.VOID_THROW.get(), level);
    }

    public VoidThrowEntity(Level level, double x, double y, double z) {
        this(MoreAvaritiaModEntities.VOID_THROW.get(), level);
        setPos(x, y, z);
    }

    @NotNull
    protected Item getDefaultItem() {
        return ModItems.endest_pearl.get();
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket((Entity)this);
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = getItemRaw();
        return itemstack.isEmpty() ? (ParticleOptions)ParticleTypes.PORTAL : (ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 3) {
            ParticleOptions particleoptions = getParticle();
            for (int i = 0; i < 8; i++)
                level().addParticle(particleoptions, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    protected void onHitEntity(EntityHitResult pos) {
        super.onHitEntity(pos);
        Entity entity = pos.getEntity();
        entity.hurt(damageSources().thrown((Entity)this, getOwner()), 0.0F);
        if (!(level()).isClientSide) {
            VoidEntity ent;
            if (this.shooter != null) {
                ent = new VoidEntity(level(), this.shooter);
            } else {
                ent = new VoidEntity(level());
            }
            Direction dir = entity.getDirection();
            Vec3 offset;
            offset = new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ());
            if (this.shooter != null)
                ent.setUser(this.shooter);
            ent.moveTo(entity.getX() + offset.x * 0.25D, entity.getY() + offset.y * 0.25D, entity.getZ() + offset.z * 0.25D, entity.getYRot(), 0.0F);
            level().addFreshEntity(ent);
            remove(Entity.RemovalReason.KILLED);
        }
    }

    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        if (!(level()).isClientSide) {
            VoidEntity ent;
            if (this.shooter != null) {
                ent = new VoidEntity(level(), this.shooter);
            } else {
                ent = new VoidEntity(level());
            }
            Direction dir = result.getDirection();
            Vec3 offset;
            offset = new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ());
            if (this.shooter != null)
                ent.setUser(this.shooter);
            ent.moveTo(pos.getX() + offset.x * 0.25D, pos.getY() + offset.y * 0.25D, pos.getZ() + offset.z * 0.25D, getYRot(), 0.0F);
            level().addFreshEntity(ent);
            remove(Entity.RemovalReason.KILLED);
        }
    }
}
