package net.wzz.more_avaritia.util;

import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class MoreAvaritiaList {
    private static Set<String> canFly = new HashSet<>();
    public static void addFly(Entity entity) {
        if (!canFly.contains(entity.getCachedUniqueIdString()))
            canFly.add(entity.getCachedUniqueIdString());
    }
    public static void removeFly(Entity entity) {
        canFly.remove(entity.getCachedUniqueIdString());
    }
    public static boolean isFly(Entity entity) {
        return canFly.contains(entity.getCachedUniqueIdString());
    }
}
