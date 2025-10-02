
package net.wzz.more_avaritia.item.bow;

import committee.nova.mods.avaritia.common.entity.arrow.HeavenArrowEntity;
import committee.nova.mods.avaritia.common.entity.arrow.TraceArrowEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityBowItem extends committee.nova.mods.avaritia.common.item.tools.infinity.InfinityBowItem {
	public InfinityBowItem() {
		super();
	}

	private int shootTimer;

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
		this.shootTimer = 0;
		return super.use(p_40672_, p_40673_, p_40674_);
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
		super.onUseTick(level, entity, stack, timeLeft);
		this.shootTimer++;
		if (!level.isClientSide && entity instanceof Player player && this.shootTimer >= 10) {
			int drawTime = this.getUseDuration(stack) - timeLeft;
			drawTime = ForgeEventFactory.onArrowLoose(stack, level, player, drawTime, true);
			if (drawTime < 0) {
				return;
			}
			float VELOCITY_MULTIPLIER = 1.2F;
			float DAMAGE_MULTIPLIER = 5000.0F;
			float draw = getPowerForTime(drawTime);
			float powerForTime = draw * VELOCITY_MULTIPLIER;
			AbstractArrow arrowEntity = this.customArrow(new HeavenArrowEntity(player));
			drawTime = ForgeEventFactory.onArrowLoose(stack, level, player, drawTime, true);
			if (drawTime < 0) {
				return;
			}
			if (stack.getOrCreateTag().getBoolean("tracer") && (double) powerForTime >= 0.1) {
				arrowEntity = this.customArrow(new TraceArrowEntity(player));
			}
			arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, powerForTime * 3.0F, 0.01F);
			if (draw == 1.0F) {
				arrowEntity.setCritArrow(true);
			}
			arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * (double) DAMAGE_MULTIPLIER);
			this.addEnchant(stack, level, player, arrowEntity, powerForTime);
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + powerForTime * 0.5F);
			player.awardStat(Stats.ITEM_USED.get(this));
			this.shootTimer = 0;
		}
	}

	private void addEnchant(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity player, AbstractArrow arrowEntity, float powerForTime) {
		int j = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
		if (j > 0) {
			arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double)j * 0.5 + 0.5);
		}
		int k = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
		if (k > 0) {
			arrowEntity.setKnockback(k);
		}
		if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
			arrowEntity.setSecondsOnFire(100);
		}
		stack.hurtAndBreak(1, player, (livingEntity) -> {
			livingEntity.broadcastBreakEvent(player.getUsedItemHand());
		});
		arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		level.addFreshEntity(arrowEntity);
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("item.infinity_bow.text.1"));
	}
}
