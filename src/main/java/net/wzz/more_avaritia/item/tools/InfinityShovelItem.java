
package net.wzz.more_avaritia.item.tools;

import committee.nova.mods.avaritia.common.entity.EndestPearlEntity;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityShovelItem extends committee.nova.mods.avaritia.common.item.tools.infinity.InfinityShovelItem {
	public InfinityShovelItem() {
		super();
	}

	private int modifyTime;
	private boolean isModify;

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("item.infinity_shovel.text.1"));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
		if (p_41433_.isShiftKeyDown()) {
			modifyTime = 0;
			isModify = true;
		}
		return super.use(p_41432_, p_41433_, p_41434_);
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide && entity.isShiftKeyDown() && !isModify) {
			EndestPearlEntity pearl = (ModEntities.ENDER_PEARL.get()).create(entity.level());
			if (pearl != null) {
				pearl.setItem(new ItemStack(ModItems.endest_pearl.get()));
				pearl.setShooter(entity);
				pearl.setPos(entity.getX(), entity.getEyeY() + 0.1, entity.getZ());
				pearl.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.5F, 1.0F);
				entity.level().addFreshEntity(pearl);
			}
		}
		return super.onEntitySwing(stack, entity);
	}

	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
		super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
		if (isModify) {
			modifyTime++;
			if (modifyTime >= 30) {
				modifyTime = 0;
				isModify = false;
			}
		}
	}
}
