
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class NeutronBowItem extends BowItem {
	public NeutronBowItem() {
		super(new Properties().tab(AvaritiaModContent.TAB).stacksTo(1).fireResistant().rarity(Rarity.COMMON));
	}

	@Override
	public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
		if (p_40669_ instanceof Player player) {
			ItemStack itemstack = new ItemStack(Items.ARROW);
			int i = this.getUseDuration(p_40667_) - p_40670_;
			i = ForgeEventFactory.onArrowLoose(p_40667_, p_40668_, player, i, true);
			if (i < 0) {
				return;
			}
			float f = getPowerForTime(i);
			if (!((double) f < 0.1)) {
				if (!p_40668_.isClientSide) {
					ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
					AbstractArrow abstractarrow = arrowitem.createArrow(p_40668_, itemstack, player);
					abstractarrow = this.customArrow(abstractarrow);
					abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
					if (f == 1.0F) {
						abstractarrow.setCritArrow(true);
					}
					int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, p_40667_);
					if (j > 0) {
						abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5 + 0.5);
					}
					abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + 600);
					int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, p_40667_);
					if (k > 0) {
						abstractarrow.setKnockback(k);
					}
					abstractarrow.setKnockback(abstractarrow.getKnockback() + 1);
					if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, p_40667_) > 0) {
						abstractarrow.setSecondsOnFire(100);
					}
					p_40667_.hurtAndBreak(1, player, (p_40665_) -> {
						p_40665_.broadcastBreakEvent(player.getUsedItemHand());
					});
					abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					p_40668_.addFreshEntity(abstractarrow);
				}
				p_40668_.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
		ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
		p_40673_.startUsingItem(p_40674_);
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public int getUseDuration(ItemStack p_40680_) {
		return 18000;
	}
}
