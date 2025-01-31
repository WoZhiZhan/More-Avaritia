
package net.wzz.more_avaritia.item;

import morph.avaritia.entity.InfinityArrowEntity;
import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class InfinityBowItem extends morph.avaritia.item.tools.InfinityBowItem {
	public InfinityBowItem() {
		super(new Item.Properties().tab(AvaritiaModContent.TAB).stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
		super.onUseTick(level, entity, stack, timeLeft);
		if (entity instanceof Player player) {
			ItemStack ammoStack = player.getProjectile(stack);
			int drawTime = this.getUseDuration(stack) - timeLeft;
			drawTime = ForgeEventFactory.onArrowLoose(stack, level, player, drawTime, true);
			if (drawTime < 0) {
				return;
			}
			if (ammoStack.isEmpty()) {
				ammoStack = new ItemStack(Items.ARROW);
			}
			float VELOCITY_MULTIPLIER = 1.2F;
			float DAMAGE_MULTIPLIER = 5000.0F;
			float draw = getPowerForTime(drawTime);
			float powerForTime = draw * VELOCITY_MULTIPLIER;
			if ((double) powerForTime >= 0.1) {
				if (!level.isClientSide) {
					ArrowItem arrowitem = (ArrowItem) (ammoStack.getItem() instanceof ArrowItem ? ammoStack.getItem() : Items.ARROW);
					AbstractArrow arrowEntity = this.customArrow(arrowitem.createArrow(level, ammoStack, player));
					if (arrowEntity instanceof Arrow) {
						Arrow arrow = (Arrow) arrowEntity;
						arrow.setEffectsFromItem(ammoStack);
					} else if (arrowEntity instanceof InfinityArrowEntity) {
						InfinityArrowEntity infinityArrow = (InfinityArrowEntity) arrowEntity;
						infinityArrow.setEffectsFromItem(ammoStack);
					}
					arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, powerForTime * 3.0F, 0.01F);
					arrowEntity.setCritArrow(true);
					arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * (double) DAMAGE_MULTIPLIER);
					int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
					if (k > 0) {
						arrowEntity.setKnockback(k);
					}
					arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					level.addFreshEntity(arrowEntity);
				}
				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + powerForTime * 0.5F);
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	public void m_5551_(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (entity instanceof Player player) {
			ItemStack ammoStack = player.getProjectile(stack);
			int drawTime = this.getUseDuration(stack) - timeLeft;
			drawTime = ForgeEventFactory.onArrowLoose(stack, level, player, drawTime, true);
			if (drawTime < 0) {
				return;
			}
			if (ammoStack.isEmpty()) {
				ammoStack = new ItemStack(Items.ARROW);
			}
			float VELOCITY_MULTIPLIER = 1.2F;
			float DAMAGE_MULTIPLIER = 5000.0F;
			float draw = getPowerForTime(drawTime);
			float powerForTime = draw * VELOCITY_MULTIPLIER;
			if ((double)powerForTime >= 0.1) {
				if (!level.isClientSide) {
					ArrowItem arrowitem = (ArrowItem)(ammoStack.getItem() instanceof ArrowItem ? ammoStack.getItem() : Items.ARROW);
					AbstractArrow arrowEntity = this.customArrow(arrowitem.createArrow(level, ammoStack, player));
					if (arrowEntity instanceof Arrow) {
						Arrow arrow = (Arrow)arrowEntity;
						arrow.setEffectsFromItem(ammoStack);
					} else if (arrowEntity instanceof InfinityArrowEntity) {
						InfinityArrowEntity infinityArrow = (InfinityArrowEntity)arrowEntity;
						infinityArrow.setEffectsFromItem(ammoStack);
					}
					arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, powerForTime * 3.0F, 0.01F);
					arrowEntity.setCritArrow(true);
					arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * (double)DAMAGE_MULTIPLIER);
					int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
					if (k > 0) {
						arrowEntity.setKnockback(k);
					}
					arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					level.addFreshEntity(arrowEntity);
				}
				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + powerForTime * 0.5F);
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TranslatableComponent("bow.text"));
		//list.add(new TranslatableComponent("bow.text2"));
	}
}
