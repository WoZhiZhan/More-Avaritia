//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.wzz.more_avaritia.util;

import codechicken.lib.texture.TextureUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class MoreAvaritiaTextures implements TextureUtils.IIconRegister {
    private static TextureMap map;
    public static TextureAtlasSprite INFINITY_PICKAXE1;
    public static TextureAtlasSprite INFINITY_PICKAXE2;
    public void registerIcons(TextureMap textureMap) {
        map = textureMap;
        INFINITY_PICKAXE1 = register("more_avaritia:items/pickaxe");
        INFINITY_PICKAXE2 = register("more_avaritia:items/infinity_hammer");
    }

    private static TextureAtlasSprite register(String sprite) {
        return map.registerSprite(new ResourceLocation(sprite));
    }
}
