
package net.wzz.more_avaritia.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.wzz.more_avaritia.event.EventHandle;
import net.wzz.more_avaritia.util.ListHelper;
import net.wzz.more_avaritia.util.RainbowText;

import java.util.Comparator;
import java.util.List;

public class InfinitySwordGodItem extends TieredItem {
	public InfinitySwordGodItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 1000f;
			}

			public float getAttackDamageBonus() {
				return Float.POSITIVE_INFINITY;
			}

			public int getLevel() {
				return 1000;
			}

			public int getEnchantmentValue() {
				return 1000;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState blockstate) {
		int tier = 1000;
		if (tier < 3 && blockstate.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
			return false;
		} else if (tier < 2 && blockstate.is(BlockTags.NEEDS_IRON_TOOL)) {
			return false;
		} else {
			return tier < 1 && blockstate.is(BlockTags.NEEDS_STONE_TOOL)
					? false
					: (blockstate.is(BlockTags.MINEABLE_WITH_AXE) || blockstate.is(BlockTags.MINEABLE_WITH_HOE) || blockstate.is(BlockTags.MINEABLE_WITH_PICKAXE) || blockstate.is(BlockTags.MINEABLE_WITH_SHOVEL));
		}
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction)
				|| ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState blockstate) {
		return 1000f;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 112f, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.4, AttributeModifier.Operation.ADDITION));
			return builder.build();
		}
		return super.getDefaultAttributeModifiers(equipmentSlot);
	}

	@Override
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
		itemstack.hurtAndBreak(1, entity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		itemstack.hurtAndBreak(2, entity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TranslatableComponent("infinity_sword_god.text1"));
		list.add(new TranslatableComponent("infinity_sword_god.text2"));
		list.add(new TranslatableComponent("infinity_sword_god.text3"));
		list.add(new TranslatableComponent("infinity_sword_god.text4"));
		list.add(new TranslatableComponent("infinity_sword_god.text5"));
		list.add(new TranslatableComponent("infinity_sword_god.text6"));
	}

	@Override
	public Component getName(ItemStack p_41458_) {
		Component name = new TranslatableComponent("item.more_avaritia.infinity_sword_god");
		return new TextComponent(RainbowText.makeColour(name.getString()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity living) {
			if (player.isShiftKeyDown())
				removeEntity(living);
			else killEntity(living, player);
		} else entity.discard();
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
		super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
		if (p_41406_ instanceof Player player) {
			player.clearFire();
			player.deathTime = 0;
		}
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (entity instanceof Player player) {
			for (int i = 1; i <= 256; i++) {
				final Vec3 vec3 = new Vec3(
						(entity.level.clip(new ClipContext(entity.getEyePosition(i), entity.getEyePosition(i).add(entity.getViewVector(i).scale(i)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
						(entity.level.clip(new ClipContext(entity.getEyePosition(i), entity.getEyePosition(i).add(entity.getViewVector(i).scale(i)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
						(entity.level.clip(new ClipContext(entity.getEyePosition(i), entity.getEyePosition(i).add(entity.getViewVector(i).scale(i)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()));
				List<Entity> entities = entity.level.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(1.5d), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(vec3)))
						.toList();
				for (Entity e : entities) {
					if (e instanceof Player player1) {
						if (!EventHandle.hasItem(player1, this))
							killEntity(player1, player);
					} else {
						if (e instanceof LivingEntity living) {
							if (player.isShiftKeyDown())
								removeEntity(living);
							else killEntity(living, player);
						}
					}
				}
			}
		}
		return super.onEntitySwing(stack, entity);
	}

	public static void killEntity(LivingEntity living, Player player) {
		living.setSecondsOnFire(1000);
		if (player != null) {
			living.hurt(DamageSource.playerAttack(player), Float.POSITIVE_INFINITY);
			if (!(living instanceof Player))
				living.die(DamageSource.playerAttack(player));
			living.setLastHurtByPlayer(player);
		}
		living.setHealth(0);
		if (!(living instanceof Player))
			ListHelper.addEntityToList(living);
	}

	private void removeEntity(LivingEntity living) {
		living.setPos(-9999,-9999,-9999);
		if (!(living instanceof Player)) {
			ListHelper.addEntityToRemoveList(living);
			living.discard();
			living.setInvisible(true);
			living.removeVehicle();
			living.remove(Entity.RemovalReason.DISCARDED);
			living.onRemovedFromWorld();
			living.setRemoved(Entity.RemovalReason.DISCARDED);
		}
		living.setHealth(0);
	}
}
