
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.ForgeRegistries;
import net.wzz.more_avaritia.client.FakeInfinityArmorModel;
import net.wzz.more_avaritia.client.InfinityArmorModel;
import net.wzz.more_avaritia.event.EventHandle;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class Infinity_armor_ArmorItem extends ArmorItem {
	public Infinity_armor_ArmorItem(EquipmentSlot slot, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForSlot(EquipmentSlot slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 0;
			}

			@Override
			public int getDefenseForSlot(EquipmentSlot slot) {
				return new int[]{1024, 1024, 1024, 1024}[slot.getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 100;
			}

			@Override
			public SoundEvent getEquipSound() {
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Blocks.LAPIS_BLOCK));
			}

			@Override
			public String getName() {
				return "infinity_armor_armor";
			}

			@Override
			public float getToughness() {
				return 10f;
			}

			@Override
			public float getKnockbackResistance() {
				return 5f;
			}
		}, slot, properties);
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(new TranslatableComponent("item.infinity_armor.text.1"));
	}

	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			public HumanoidModel getArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlot armorSlot, HumanoidModel _deafult) {
				FakeInfinityArmorModel model = (armorSlot == EquipmentSlot.LEGS) ? new FakeInfinityArmorModel(FakeInfinityArmorModel.createMeshes(new CubeDeformation(1.0F), 0.0F, true).getRoot().bake(64, 64)) : new FakeInfinityArmorModel(FakeInfinityArmorModel.createMeshes(new CubeDeformation(1.0F), 0.0F, false).getRoot().bake(64, 64));
				model.update(entityLiving, itemstack, armorSlot);
				return model;
			}
		});
	}

	@Override
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		super.onArmorTick(stack, level, player);
		if (EventHandle.hasInfinityArmor(player)) {
			player.deathTime = 0;
		}
	}

	public static class Helmet extends Infinity_armor_ArmorItem {
		public Helmet() {
			super(EquipmentSlot.HEAD, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity___layer_1.png";
		}
	}

	public static class Chestplate extends Infinity_armor_ArmorItem {
		public Chestplate() {
			super(EquipmentSlot.CHEST, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity___layer_1.png";
		}
	}

	public static class Leggings extends Infinity_armor_ArmorItem {
		public Leggings() {
			super(EquipmentSlot.LEGS, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity___layer_2.png";
		}
	}

	public static class Boots extends Infinity_armor_ArmorItem {
		public Boots() {
			super(EquipmentSlot.FEET, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "more_avaritia:textures/models/armor/infinity___layer_1.png";
		}
	}
}
