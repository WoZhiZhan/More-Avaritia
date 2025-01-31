
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class InfinityAppleItem extends Item {
	public InfinityAppleItem() {
		super(new Item.Properties().tab(AvaritiaModContent.TAB).stacksTo(64).fireResistant().rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(20).saturationMod(20f).alwaysEat()

				.build()));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
		p_41411_.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10000, 5));
		p_41411_.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10000, 5));
		return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
	}
}
