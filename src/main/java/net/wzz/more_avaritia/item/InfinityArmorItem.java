
package net.wzz.more_avaritia.item;

import committee.nova.mods.avaritia.common.entity.ImmortalItemEntity;
import committee.nova.mods.avaritia.init.registry.ModArmorMaterial;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.wzz.more_avaritia.entity.model.InfinityArmorModel;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.util.InfinityUtils;
import net.wzz.more_avaritia.util.RainbowText;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class InfinityArmorItem extends ArmorItem {
	public static final ArmorMaterial INFINITY_ARMOR = new ModArmorMaterial.SimpleArmorMaterial("infinity_armor_super", 15, Util.make(new EnumMap(ArmorItem.Type.class), (p_266655_) -> {
		p_266655_.put(Type.BOOTS, 3);
		p_266655_.put(Type.LEGGINGS, 6);
		p_266655_.put(Type.CHESTPLATE, 8);
		p_266655_.put(Type.HELMET, 3);
	}), 1000, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0F, 1.0F, () -> Ingredient.of(MoreAvaritiaModItems.COMPRESSED_INFINITY_INGOT.get()));

	public InfinityArmorItem(Type type) {
		super(INFINITY_ARMOR,type,(new Item.Properties()).rarity(ModRarities.COSMIC).fireResistant().stacksTo(1)
		);
	}

	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			public @NotNull HumanoidModel<Player> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlot armorSlot, HumanoidModel _deafult) {
				InfinityArmorModel model = armorSlot == EquipmentSlot.LEGS ? new InfinityArmorModel(InfinityArmorModel.createMesh(new CubeDeformation(1.0F), 0.0F, true).getRoot().bake(64, 64)) : new InfinityArmorModel(InfinityArmorModel.createMesh(new CubeDeformation(1.0F), 0.0F, false).getRoot().bake(64, 64));
				model.update(entityLiving, itemstack, armorSlot);
				return model;
			}
		});
	}

	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
		super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
		if (p_41406_ instanceof Player player) {
			if (InfinityUtils.hasInfinityArmor(player)) {
				player.clearFire();
				player.getPersistentData().putBoolean("isInfinityArmorFly", true);
			}
		}
	}

	public boolean isFoil(@NotNull ItemStack pStack) {
		return false;
	}

	public boolean isDamageable(ItemStack stack) {
		return false;
	}

	public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderMan) {
		return true;
	}

	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}

	public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
		return ModRarities.COSMIC;
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
		if (this.type.getSlot() == EquipmentSlot.HEAD) {
			tooltip.add(Component.literal(""));
			ChatFormatting var10001 = ChatFormatting.BLUE;
			tooltip.add(Component.literal(var10001 + "+" + ChatFormatting.ITALIC + RainbowText.makeSANIC(I18n.get("tooltip.infinity", new Object[0])) + ChatFormatting.RESET + ChatFormatting.BLUE + "% ").append(I18n.get("effect.minecraft.night_vision", new Object[0])));
		}

		if (this.type.getSlot() == EquipmentSlot.CHEST) {
			tooltip.add(Component.literal(""));
			ChatFormatting var5 = ChatFormatting.BLUE;
			tooltip.add(Component.literal(var5 + "+" + ChatFormatting.ITALIC + RainbowText.makeSANIC(I18n.get("tooltip.infinity", new Object[0])) + ChatFormatting.RESET + ChatFormatting.BLUE + "% ").append(I18n.get("attribute.name.generic.flying_speed", new Object[0])));
		}

		if (this.type.getSlot() == EquipmentSlot.LEGS) {
			tooltip.add(Component.literal(""));
			ChatFormatting var6 = ChatFormatting.BLUE;
			tooltip.add(Component.literal(var6 + "+" + ChatFormatting.ITALIC + RainbowText.makeSANIC(I18n.get("tooltip.infinity", new Object[0])) + ChatFormatting.RESET + ChatFormatting.BLUE + "% ").append(I18n.get("attribute.name.generic.walking_speed", new Object[0])));
		}

		if (this.type.getSlot() == EquipmentSlot.FEET) {
			tooltip.add(Component.literal(""));
			ChatFormatting var7 = ChatFormatting.BLUE;
			tooltip.add(Component.literal(var7 + "+" + ChatFormatting.ITALIC + RainbowText.makeSANIC(I18n.get("tooltip.infinity", new Object[0])) + ChatFormatting.RESET + ChatFormatting.BLUE + "% ").append(I18n.get("attribute.name.generic.movement_speed", new Object[0])));
		}

		super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
	}

	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
		return ImmortalItemEntity.create(ModEntities.IMMORTAL.get(), level, location.getX(), location.getY(), location.getZ(), stack);
	}


	public static class Helmet extends InfinityArmorItem {
		public Helmet() {
			super(Type.HELMET);
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_armor.png";
		}
	}

	public static class Chestplate extends InfinityArmorItem {
		public Chestplate() {
			super(Type.CHESTPLATE);
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_armor.png";
		}
	}

	public static class Leggings extends InfinityArmorItem {
		public Leggings() {
			super(Type.LEGGINGS);
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_armor.png";
		}
	}

	public static class Boots extends InfinityArmorItem {
		public Boots() {
			super(Type.BOOTS);
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_armor.png";
		}
	}
}
