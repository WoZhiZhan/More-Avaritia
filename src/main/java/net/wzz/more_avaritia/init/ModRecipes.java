package net.wzz.more_avaritia.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wzz.more_avaritia.MoreAvaritiaMod;
import net.wzz.more_avaritia.recipe.InfinityEnergyRecipe;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreAvaritiaMod.MODID);

    public static final RegistryObject<RecipeSerializer<InfinityEnergyRecipe>> INFINITY_ENERGY_SERIALIZER =
            RECIPE_SERIALIZERS.register("infinity_energy", () -> new SimpleCraftingRecipeSerializer<InfinityEnergyRecipe>(InfinityEnergyRecipe::new));
}
