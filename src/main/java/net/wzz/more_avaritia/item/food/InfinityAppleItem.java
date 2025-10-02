
package net.wzz.more_avaritia.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InfinityAppleItem extends Item {
	public InfinityAppleItem() {
		super(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.EPIC).food((new FoodProperties.Builder()).nutrition(20).saturationMod(20f).alwaysEat().meat().build()));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41414_) {
		p_41414_.heal(p_41414_.getMaxHealth());
		p_41414_.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 9999999, 4));
		return super.finishUsingItem(p_41409_, p_41410_, p_41414_);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}
