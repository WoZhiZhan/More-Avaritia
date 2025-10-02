
package net.wzz.more_avaritia.item.tools;

import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.wzz.more_avaritia.entity.VoidSpeedEntity;
import net.wzz.more_avaritia.tiers.ModTiers;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResoundSwordItem extends SwordItem {

	public ResoundSwordItem() {
		super(ModTiers.INFINITY_SWORD, 89, -1.9F, (new Properties()).stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	public boolean isFoil(@NotNull ItemStack pStack) {
		return false;
	}

	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity living) {
			living.hurt(player.damageSources().playerAttack(player), 100.0f);
			InfinityUtils.sweepAttackNormal(player.level(), player, living, 20);
			InfinityUtils.playSound(player.level(), SoundEvents.TRIDENT_THROW, player);
			BlockPos pos = entity.getOnPos();
			if (!(player.level()).isClientSide) {
				VoidSpeedEntity ent = new VoidSpeedEntity(player.level(), player);
				ent.setUser(player);
				ent.setPos(pos.getX(), pos.getY(), pos.getZ());
				player.level().addFreshEntity(ent);
			}
		}
		return false;
	}

	public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
		return ModRarities.COSMIC;
	}

	public boolean isDamageable(ItemStack stack) {
		return false;
	}

	public int getEnchantmentValue(ItemStack stack) {
		return 0;
	}


	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("resound_sword.text.1"));
	}
}
