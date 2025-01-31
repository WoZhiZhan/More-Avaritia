package net.wzz.more_avaritia.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import java.util.Objects;

public class EventUtil {
    public static float getHealth(EntityLivingBase living) {
        if (living instanceof EntityWolf)
            return living.getDataManager().get(EntityLivingBase.HEALTH);
        if (living instanceof EntityPlayer) {
            if (HasItemFromSlot.hasArmor(living)) {
                if (!living.world.loadedEntityList.contains(living))
                    living.world.loadedEntityList.add(living);
                living.isDead = false;
                living.setHealth(living.getMaxHealth());
                return living.getMaxHealth();
            }
        }
        if (living.getEntityData().getBoolean("MoreDead"))
            return 0;
        return living.getDataManager().get(EntityLivingBase.HEALTH);
    }
    public static void killEntityLiving(EntityLivingBase entity, EntityPlayer sourcePlayer) {
        entity.setFire(100);
        entity.onDeathUpdate();
        entity.getDataManager().set(EntityLivingBase.HEALTH, 0F);
        entity.attackEntityFrom(DamageSource.causePlayerDamage(sourcePlayer), (float) Double.POSITIVE_INFINITY);
        entity.onDeath(DamageSource.OUT_OF_WORLD);
        entity.setHealth(0);
        entity.getEntityData().setBoolean("MoreDead",true);
    }
    public static void removeEntity(EntityLivingBase living) {
        if (living instanceof EntityPlayer)
            return;
        living.isDead = true;
        living.setInvisible(true);
        living.setPosition(-9999,-999,-999);
        living.world.loadedEntityList.remove(living);
        living.world.removeEntity(living);
        Objects.requireNonNull(living.world.getChunkProvider().
                getLoadedChunk(living.chunkCoordX, living.chunkCoordZ)).removeEntity(living);
    }
}
