package net.wzz.more_avaritia.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;
import committee.nova.mods.avaritia.api.client.model.PerspectiveModelState;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import net.wzz.more_avaritia.vec.Vector3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ItemENUM {
    public static final PerspectiveModelState DEFAULT_ITEM;
    public static final PerspectiveModelState DEFAULT_TOOL;
    public static final PerspectiveModelState DEFAULT_BOW;
    public static final PerspectiveModelState DEFAULT_HANDHELD_ROD;
    private static final Transformation flipX;
    public static Transformation create(float tx, float ty, float tz, float rx, float ry, float rz, float s) {
        return create(new Vector3f(tx / 16.0F, ty / 16.0F, tz / 16.0F), new Vector3f(rx, ry, rz), new Vector3f(s, s, s));
    }

    public static Transformation create(Vector3 transform, Vector3 rotation, Vector3 scale) {
        return create(transform.vector3f(), rotation.vector3f(), scale.vector3f());
    }

    public static Transformation create(Vector3f transform, Vector3f rotation, Vector3f scale) {
        return new Transformation(transform, (new Quaternionf()).rotationXYZ((float)((double)rotation.x() * 0.017453292519943), (float)((double)rotation.y() * 0.017453292519943), (float)((double)rotation.z() * 0.017453292519943)), scale, (Quaternionf)null);
    }

    public static Transformation create(ItemTransform transform) {
        return ItemTransform.NO_TRANSFORM.equals(transform) ? Transformation.identity() : create(transform.translation, transform.rotation, transform.scale);
    }

    public static Transformation flipLeft(Transformation transform) {
        return flipX.compose(transform).compose(flipX);
    }

    static {
        flipX = new Transformation(null, null, new Vector3f(-1.0F, 1.0F, 1.0F), (Quaternionf)null);
        Map<ItemDisplayContext, Transformation> map = new HashMap<>();
        DEFAULT_ITEM = new PerspectiveModelState(ImmutableMap.copyOf(map));
//        map = new HashMap<>();
//        Transformation thirdPerson = create(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
//        Transformation firstPerson = create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
//        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
//        map.put(ItemDisplayContext.HEAD, create(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F));
//        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, thirdPerson);
//        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flipLeft(thirdPerson));
//        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, firstPerson);
//        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, flipLeft(firstPerson));
        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.FIXED, create(0.0F, 0.0F, 0.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, create(0.0F, 4.0F, 0.5F, 0.0F, -90.0F, 55.0F, 0.85F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, create(0.0F, 4.0F, 0.5F, 0.0F, 90.0F, -55.0F, 0.85F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, 90.0F, -25.0F, 0.68F));
        DEFAULT_TOOL = new PerspectiveModelState(ImmutableMap.copyOf(map));
        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.FIXED, create(0.0F, 0.0F, 0.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, create(-1.0F, -2.0F, 2.5F, -80.0F, 260.0F, -40.0F, 0.9F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, create(-1.0F, -2.0F, 2.5F, -80.0F, -280.0F, 40.0F, 0.9F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, 90.0F, -25.0F, 0.68F));
        DEFAULT_BOW = new PerspectiveModelState(ImmutableMap.copyOf(map));
        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, create(0.0F, 4.0F, 2.5F, 0.0F, 90.0F, 55.0F, 0.85F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, create(0.0F, 4.0F, 2.5F, 0.0F, -90.0F, -55.0F, 0.85F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(0.0F, 1.6F, 0.8F, 0.0F, 90.0F, 25.0F, 0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(0.0F, 1.6F, 0.8F, 0.0F, -90.0F, -25.0F, 0.68F));
        DEFAULT_HANDHELD_ROD = new PerspectiveModelState(ImmutableMap.copyOf(map));
    }
}
