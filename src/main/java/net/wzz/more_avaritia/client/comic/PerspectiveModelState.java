package net.wzz.more_avaritia.client.comic;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Map;

public class PerspectiveModelState implements ModelState {

    public static final PerspectiveModelState IDENTITY = new PerspectiveModelState(ImmutableMap.of());

    private final Map<ItemDisplayContext, Transformation> transforms;
    private final boolean isUvLocked;

    public PerspectiveModelState(Map<ItemDisplayContext, Transformation> transforms) {
        this(transforms, false);
    }

    public PerspectiveModelState(Map<ItemDisplayContext, Transformation> transforms, boolean isUvLocked) {
        this.transforms = ImmutableMap.copyOf(transforms);
        this.isUvLocked = isUvLocked;
    }

    public Transformation getTransform(ItemDisplayContext context) {
        return transforms.getOrDefault(context, Transformation.identity());
    }

    @Override
    public boolean isUvLocked() {
        return isUvLocked;
    }
}
