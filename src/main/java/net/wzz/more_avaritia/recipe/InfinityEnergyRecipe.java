package net.wzz.more_avaritia.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.wzz.more_avaritia.init.ModItems;
import net.wzz.more_avaritia.init.ModRecipes;

/**
 * 无尽能源 + 任意物品：复制目标物品，目标物品保留，无尽能源损失 1 点耐久。
 */
public class InfinityEnergyRecipe extends CustomRecipe {
    public InfinityEnergyRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack energy = ItemStack.EMPTY;
        ItemStack target = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.INFINITY_ENERGY.get())) {
                if (!energy.isEmpty() || stack.getDamageValue() >= stack.getMaxDamage()) {
                    return false;
                }
                energy = stack;
            } else {
                if (!target.isEmpty()) {
                    return false;
                }
                target = stack;
            }
        }
        return !energy.isEmpty() && !target.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        ItemStack target = getTarget(container);
        if (target.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = target.copy();
        result.setCount(2);
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && stack.is(ModItems.INFINITY_ENERGY.get())) {
                ItemStack energy = stack.copy();
                int damage = energy.getDamageValue() + 1;
                if (damage < energy.getMaxDamage()) {
                    energy.setDamageValue(damage);
                    list.set(i, energy);
                }
            }
        }
        return list;
    }

    private static ItemStack getTarget(CraftingContainer container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && !stack.is(ModItems.INFINITY_ENERGY.get())) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.INFINITY_ENERGY_SERIALIZER.get();
    }
}
