package net.wzz.more_avaritia.util;

import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    private static final List<LivingEntity> dieEntity = new ArrayList<>();
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
}
