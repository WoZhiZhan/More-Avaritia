
package net.wzz.more_avaritia.item.tools;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wzz.more_avaritia.entity.VoidThrowEntity;
import net.wzz.more_avaritia.init.MoreAvaritiaModEntities;

public class RodRulingItem extends Item {
	public RodRulingItem() {
		super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack p_41454_) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
		p_41433_.startUsingItem(p_41434_);
		return super.use(p_41432_, p_41433_, p_41434_);
	}

	@Override
	public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity entity, int p_41415_) {
		super.releaseUsing(p_41412_, p_41413_, entity, p_41415_);
		if (!entity.level().isClientSide) {
			VoidThrowEntity voidEntity = MoreAvaritiaModEntities.VOID_THROW.get().create(p_41413_);
			if (voidEntity != null) {
				voidEntity.setShooter(entity);
				voidEntity.setPos(entity.getX(), entity.getEyeY() + 0.1, entity.getZ());
				voidEntity.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.5F, 1.0F);
				entity.level().addFreshEntity(voidEntity);
			}
		}
		entity.playSound(SoundEvents.EGG_THROW, 1, 1);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}
