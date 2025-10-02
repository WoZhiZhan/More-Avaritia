
package net.wzz.more_avaritia.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.wzz.more_avaritia.util.InfinityUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class InfinityFakeItem extends ArmorItem {
	private int healTime;
	public InfinityFakeItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return 0;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{1024, 1024, 1024, 1024}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 1000;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.EMPTY;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}

			@Override
			public String getName() {
				return "infinity_fake";
			}

			@Override
			public float getToughness() {
				return 10f;
			}

			@Override
			public float getKnockbackResistance() {
				return 5f;
			}
		}, type, properties);
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("item.infinity_fake.text.1"));
	}

	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
		super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
		if (p_41406_ instanceof Player player && InfinityUtils.hasFakeInfinityArmor(player)) {
			healTime++;
			if (healTime >= 25) {
				healTime = 0;
				player.heal(20.0f);
			}
		} else if (healTime != 0) healTime = 0;
	}

	public static class Helmet extends InfinityFakeItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_fake_layer_1.png";
		}
	}

	public static class Chestplate extends InfinityFakeItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_fake_layer_1.png";
		}
	}

	public static class Leggings extends InfinityFakeItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_fake_layer_2.png";
		}
	}

	public static class Boots extends InfinityFakeItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity_fake_layer_1.png";
		}
	}
}
