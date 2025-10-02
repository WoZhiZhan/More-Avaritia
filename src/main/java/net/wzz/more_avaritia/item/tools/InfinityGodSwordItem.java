
package net.wzz.more_avaritia.item.tools;

import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.util.InfinityFont;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class InfinityGodSwordItem extends InfinitySwordItem {
	public InfinityGodSwordItem() {
		super();
	}

	@Override
	public float getDestroySpeed(ItemStack p_43288_, BlockState p_43289_) {
		return Float.POSITIVE_INFINITY;
	}

	@Override
	public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState p_43298_) {
		return true;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return true;
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (entity instanceof Player player) {
			for (int i = 1; i <= 256; i++) {
				final Vec3 vec3 = new Vec3(
						(entity.level().clip(new ClipContext(entity.getEyePosition(i), entity.getEyePosition(i).add(entity.getViewVector(i).scale(i)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
						(entity.level().clip(new ClipContext(entity.getEyePosition(i), entity.getEyePosition(i).add(entity.getViewVector(i).scale(i)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
						(entity.level().clip(new ClipContext(entity.getEyePosition(i), entity.getEyePosition(i).add(entity.getViewVector(i).scale(i)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()));
				List<Entity> entities = entity.level().getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(1.2d), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(vec3)))
						.toList();
				for (Entity e : entities) {
					if (e instanceof Player player1) {
						if (!InfinityUtils.hasItem(player1, this))
							InfinityUtils.easyAttack(player1, player);
					} else {
						if (e instanceof LivingEntity living) {
							if (player.isShiftKeyDown())
								InfinityUtils.superAttack(living);
							else InfinityUtils.easyAttack(living, player);
						}
					}
				}
			}
		}
		return super.onEntitySwing(stack, entity);
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.1"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.2"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.3"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.4"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.5"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.6"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.7"));
		p_41423_.add(Component.translatable("item.infinity_sword_god.text.8"));
	}

	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
		super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
		if (p_41406_ instanceof Player player) {
			player.clearFire();
			if (player.getMainHandItem().getItem().equals(MoreAvaritiaModItems.INFINITY_GOD_SWORD.get())) {
				if (!player.getAbilities().mayfly) {
					player.getAbilities().mayfly = true;
					player.getPersistentData().putBoolean("isGodInfinity", true);
					player.onUpdateAbilities();
				}
			} else if ((player.getAbilities().mayfly || player.getAbilities().flying) && !player.isCreative() && player.getPersistentData().getBoolean("isGodInfinity")) {
				player.getAbilities().mayfly = false;
				player.getAbilities().flying = false;
				player.getPersistentData().putBoolean("isGodInfinity", false);
				player.onUpdateAbilities();
			}
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (!entity.level().isClientSide && entity instanceof Player victim) {
			if (!victim.isCreative() && !victim.isDeadOrDying() && victim.getHealth() > 0.0F && !InfinityUtils.isInfinite(victim)) {
				victim.getCombatTracker().recordDamage(player.damageSources().source(ModDamageTypes.INFINITY, player, victim), victim.getHealth());
				victim.setHealth(0.0F);
				victim.die(player.damageSources().source(ModDamageTypes.INFINITY, player, victim));
				return true;
			}
		}
		if (entity instanceof LivingEntity living) {
			InfinityUtils.sweepAttack(living.level(), player, living);
			InfinityUtils.easyAttack(living, player);
		}
		return false;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack heldItem = player.getItemInHand(hand);
		if (!player.isShiftKeyDown())
			InfinityUtils.aoeAttack(player, 100f, Float.POSITIVE_INFINITY, true, ModConfig.isSwordAttackLightning.get());
		else  {
			InfinityUtils.aoeAttack(player, 200f, Float.POSITIVE_INFINITY, true, true);
			AABB aabb = player.getBoundingBox().deflate(400);
			List<Entity> toAttack = player.level().getEntities(player, aabb);
			toAttack.stream().filter((entity) -> entity instanceof ItemEntity).forEach((entity) -> {
				entity.setPos(player.getX(), player.getY() + 1, player.getZ());
			});
		}
		level.playSound(player, player.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 5.0F);
		return InteractionResultHolder.success(heldItem);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@NotNull
			public Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
				return InfinityFont.getFont();
			}
		});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}
