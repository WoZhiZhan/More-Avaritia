package net.wzz.more_avaritia.util;

import codechicken.lib.texture.TextureUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.wzz.more_avaritia.item.ItemSuperInfinityArmor;

public class MoreAvaritiaRegister implements TextureUtils.IIconRegister {
    private static TextureMap map;
    public static ItemArmor infinity_helmet = new ItemSuperInfinityArmor.Helmet();

    public static ItemArmor infinity_chestplate = new ItemSuperInfinityArmor.Chestplate();

    public static ItemArmor infinity_pants = new ItemSuperInfinityArmor.Legs();
    public static TextureAtlasSprite INFINITY_ARMOR_MASK;
    public static TextureAtlasSprite INFINITY_ARMOR_MASK_INV;
    public static TextureAtlasSprite INFINITY_ARMOR_MASK_WINGS;
    public static TextureAtlasSprite INFINITY_SINGULARITY;
    public static TextureAtlasSprite INFINITY_CATALYST;
    public static TextureAtlasSprite AXE;
    public static TextureAtlasSprite SHOVEL;
    public static TextureAtlasSprite HOE;
    public static TextureAtlasSprite PICKAXE;

    public static ItemArmor infinity_boots = new ItemSuperInfinityArmor.Boots();
    public void registerIcons(TextureMap textureMap) {
        map = textureMap;
        INFINITY_ARMOR_MASK = register("more_avaritia:models/infinity_armor_mask");
        INFINITY_ARMOR_MASK_INV = register("more_avaritia:models/infinity_armor_mask_inv");
        INFINITY_ARMOR_MASK_WINGS = register("more_avaritia:models/infinity_armor_mask_wings");
        INFINITY_SINGULARITY = register("more_avaritia:items/wei_biao_ti_-1");
        INFINITY_CATALYST = register("more_avaritia:items/coal_layer_1");
        AXE = register("more_avaritia:items/axe");
        SHOVEL = register("more_avaritia:items/shovel");
        HOE = register("more_avaritia:items/hoe");
        PICKAXE = register("more_avaritia:items/pickaxe");
    }
    private static TextureAtlasSprite register(String sprite) {
        return map.registerSprite(new ResourceLocation(sprite));
    }
    public static void init() {
        infinity_helmet = new ItemSuperInfinityArmor.Helmet();
        ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:a", "inventory");
        ModelLoader.registerItemVariants(infinity_helmet, sword);
        ModelLoader.setCustomMeshDefinition(infinity_helmet, (stack) -> sword);
        ForgeRegistries.ITEMS.register(infinity_helmet.setRegistryName("more_avaritia", "super_infinity_helmet"));
        infinity_chestplate = new ItemSuperInfinityArmor.Chestplate();
        ModelResourceLocation sword1 = new ModelResourceLocation("more_avaritia:aa", "inventory");
        ModelLoader.registerItemVariants(infinity_chestplate, sword1);
        ModelLoader.setCustomMeshDefinition(infinity_chestplate, (stack) -> sword1);
        ForgeRegistries.ITEMS.register(infinity_chestplate.setRegistryName("more_avaritia", "super_infinity_chestplate"));
        ModelLoader.setCustomModelResourceLocation(infinity_chestplate,0,new ModelResourceLocation("more_avaritia:aa","inventory"));
        infinity_pants = new ItemSuperInfinityArmor.Legs();
        ForgeRegistries.ITEMS.register(infinity_pants.setRegistryName("more_avaritia", "super_infinity_pants"));
        ModelLoader.setCustomModelResourceLocation(infinity_pants,0,new ModelResourceLocation("more_avaritia:aaa","inventory"));
        infinity_boots = new ItemSuperInfinityArmor.Boots();
        infinity_boots.setUnlocalizedName("super_infinity_boots");
        ForgeRegistries.ITEMS.register(infinity_boots.setRegistryName("more_avaritia", "super_infinity_boots"));
        ModelLoader.setCustomModelResourceLocation(infinity_boots,0,new ModelResourceLocation("more_avaritia:aaaa","inventory"));
    }
}
