package net.wzz.more_avaritia.util;

import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ListUtils {
    private static final Set<LivingEntity> dieEntity = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    public static void addDieEntity(LivingEntity living) {
        if (living == null)
            return;
        dieEntity.add(living);
    }

    public static boolean isDieEntity(LivingEntity living) {
        if (living == null)
            return false;
        return dieEntity.contains(living);
    }

    public static void removeDieEntity(LivingEntity living) {
        if (living == null)
            return;
        dieEntity.remove(living);
    }
}
