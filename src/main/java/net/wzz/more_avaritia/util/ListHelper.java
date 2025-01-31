package net.wzz.more_avaritia.util;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {
    private static final List<Entity> entityList = new ArrayList<>();
    private static final List<Entity> entityRemoveList = new ArrayList<>();
    public static void addEntityToList(Entity entity) {
        if (entity != null)
            entityList.add(entity);
    }
    public static void removeEntityToList(Entity entity) {
        if (entity != null)
            entityList.remove(entity);
    }
    public static boolean entityContainsInList(Entity entity) {
        if (entity == null)
            return false;
        return entityList.contains(entity);
    }
    public static void addEntityToRemoveList(Entity entity) {
        if (entity != null)
            entityRemoveList.add(entity);
    }
    public static void removeEntityToRemoveList(Entity entity) {
        if (entity != null)
            entityRemoveList.remove(entity);
    }
    public static boolean entityContainsInRemoveList(Entity entity) {
        if (entity == null)
            return false;
        return entityRemoveList.contains(entity);
    }
}
