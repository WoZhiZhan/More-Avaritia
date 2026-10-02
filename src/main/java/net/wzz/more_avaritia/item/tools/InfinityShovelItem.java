
package net.wzz.more_avaritia.item.tools;

import committee.nova.mods.avaritia.common.entity.EndestPearlEntity;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.wzz.more_avaritia.client.IItemType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityShovelItem extends committee.nova.mods.avaritia.common.item.tools.infinity.InfinityShovelItem implements IItemType {
	private static final String SUPPRESS_KEY = "more_avaritia_shovel_suppress";

	public InfinityShovelItem() {
		super();
	}

	@Override
	public Type getItemType() {
		return Type.TOOL;
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("item.infinity_shovel.text.1"));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
		if (p_41433_.isShiftKeyDown()) {
			p_41433_.getPersistentData().putInt(SUPPRESS_KEY, 30);
		}
		return super.use(p_41432_, p_41433_, p_41434_);
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide && entity.isShiftKeyDown()
				&& entity.getPersistentData().getInt(SUPPRESS_KEY) <= 0) {
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
		if (p_41406_ instanceof Player player) {
			CompoundTag data = player.getPersistentData();
			int suppress = data.getInt(SUPPRESS_KEY);
			if (suppress > 0) {
				data.putInt(SUPPRESS_KEY, suppress - 1);
			}
		}
	}
}
