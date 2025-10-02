
package net.wzz.more_avaritia.item.tools;

import committee.nova.mods.avaritia.common.entity.ImmortalItemEntity;
import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.wzz.more_avaritia.tiers.ModTiers;
import net.wzz.more_avaritia.util.InfinityUtils;
import net.wzz.more_avaritia.util.RainbowText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinitySwordItem extends SwordItem {

	public InfinitySwordItem() {
		super(ModTiers.INFINITY_SWORD, 0, -2.4F, (new Item.Properties()).stacksTo(1).fireResistant());
	}

	public boolean isFoil(@NotNull ItemStack pStack) {
		return false;
	}

	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
		return ImmortalItemEntity.create(ModEntities.IMMORTAL.get(), level, location.getX(), location.getY(), location.getZ(), stack);
	}

	public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity victim, LivingEntity livingEntity) {
		Level level = livingEntity.level();
		if (!level.isClientSide) {
			label27:
			{
				if (victim instanceof EnderDragon dragon) {
                    if (livingEntity instanceof Player player) {
                        dragon.hurt(dragon.head, player.damageSources().source(ModDamageTypes.INFINITY, player, victim), Float.POSITIVE_INFINITY);
						dragon.setHealth(0.0F);
						break label27;
					}
				}

				if (victim instanceof Player pvp) {
                    if (InfinityUtils.isInfinite(pvp)) {
						victim.hurt(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), 4.0F);
					} else {
						victim.hurt(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), Float.POSITIVE_INFINITY);
					}
				} else {
					victim.hurt(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), Float.POSITIVE_INFINITY);
				}
			}
			victim.lastHurtByPlayerTime = 60;
			victim.getCombatTracker().recordDamage(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), victim.getHealth());
			if (victim instanceof Player victimP) {
                if (InfinityUtils.isInfinite(victimP)) {
					victimP.level().explode(livingEntity, (double) victimP.getBlockX(), (double) victimP.getBlockY(), (double) victimP.getBlockZ(), 25.0F, ExplosionInteraction.BLOCK);
					return true;
				}
			}
			InfinityUtils.sweepAttack(level, livingEntity, victim);
			victim.setHealth(0.0F);
			victim.die(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim));
		}
		return true;
	}

	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (!entity.level().isClientSide && entity instanceof Player victim) {
			if (!victim.isCreative() && !victim.isDeadOrDying() && victim.getHealth() > 0.0F && !InfinityUtils.isInfinite(victim)) {
				victim.getCombatTracker().recordDamage(player.damageSources().source(ModDamageTypes.INFINITY, player, victim), victim.getHealth());
				victim.setHealth(0.0F);
				victim.die(player.damageSources().source(ModDamageTypes.INFINITY, player, victim));
				return true;
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


	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack heldItem = player.getItemInHand(hand);
		if (!level.isClientSide) {
			InfinityUtils.aoeAttack(player, (float) ModConfig.swordAttackRange.get(), (float) ModConfig.swordRangeDamage.get(), true, ModConfig.isSwordAttackLightning.get());
		}
		level.playSound(player, player.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 5.0F);
		return InteractionResultHolder.success(heldItem);
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		String text1 = Component.translatable("infinity_sword.text.1").getString();
		p_41423_.add(Component.literal(RainbowText.makeColour(text1)));
		p_41423_.add(Component.translatable("infinity_sword.text.2"));
	}
}
