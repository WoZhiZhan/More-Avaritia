
package net.wzz.more_avaritia.item;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import morph.avaritia.Avaritia;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ModHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.util.ModelArmorSuperInfinity;
import net.wzz.more_avaritia.util.RainbowText;

import java.util.List;

public class ItemSuperInfinityArmor extends ItemArmor {
	public static final ItemArmor.ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial("super_avaritia", "", 9999, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

	public final EntityEquipmentSlot slot;

	public ItemSuperInfinityArmor(EntityEquipmentSlot slot) {
		super(infinite_armor, 0, slot);
		this.slot = slot;
		setCreativeTab(Avaritia.tab);
	}

	public static class Helmet extends ItemSuperInfinityArmor {
		public Helmet() {
			super(EntityEquipmentSlot.HEAD);
			setCreativeTab(Avaritia.tab);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack p_getItemStackDisplayName_1_) {
			return I18n.format("item.super_infinity_helmet");
		}
	}
	public static class Chestplate extends ItemSuperInfinityArmor {
		public Chestplate() {
			super(EntityEquipmentSlot.CHEST);
			setCreativeTab(Avaritia.tab);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack p_getItemStackDisplayName_1_) {
			return I18n.format("item.super_infinity_chestplate");
		}
	}
	public static class Legs extends ItemSuperInfinityArmor {
		public Legs() {
			super(EntityEquipmentSlot.LEGS);
			setCreativeTab(Avaritia.tab);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack p_getItemStackDisplayName_1_) {
			return I18n.format("item.super_infinity_pants");
		}
	}
	public static class Boots extends ItemSuperInfinityArmor {
		public Boots() {
			super(EntityEquipmentSlot.FEET);
			setCreativeTab(Avaritia.tab);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack p_getItemStackDisplayName_1_) {
			return I18n.format("item.super_infinity_boots");
		}
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "more_avaritia:textures/models/armor/infinity_armor.png";
	}

	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, 0);
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (this.armorType == EntityEquipmentSlot.HEAD) {
			player.setAir(300);
			player.getFoodStats().addStats(20, 20.0F);
			PotionEffect nv = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
			if (nv == null) {
				nv = new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, false, false);
				player.addPotionEffect(nv);
			}
			nv.duration = 300;
		} else if (this.armorType == EntityEquipmentSlot.CHEST) {
			player.capabilities.allowFlying = true;
			List<PotionEffect> effects = Lists.newArrayList(player.getActivePotionEffects());
			for (PotionEffect potion : Collections2.filter(effects, potion -> potion.getPotion().isBadEffect())) {
				if (ModHelper.isHoldingCleaver((EntityLivingBase) player) && potion.getPotion().equals(MobEffects.MINING_FATIGUE))
					continue;
				player.removePotionEffect(potion.getPotion());
			}
		} else if (this.armorType == EntityEquipmentSlot.LEGS &&
				player.isBurning()) {
			player.extinguish();
		}
	}

	public EnumRarity getRarity(ItemStack stack) {
		return ModItems.COSMIC_RARITY;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot, ModelBiped _deafult) {
		ModelArmorSuperInfinity model = (armorSlot == EntityEquipmentSlot.LEGS) ? ModelArmorSuperInfinity.legModel : ModelArmorSuperInfinity.armorModel;
		model.update(entityLiving, itemstack, armorSlot);
		return (ModelBiped) model;
	}

	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this.slot == EntityEquipmentSlot.FEET) {
			tooltip.add("");
			tooltip.add(TextFormatting.BLUE + "+" + TextFormatting.ITALIC + RainbowText.makeColour("SANIC") + TextFormatting.RESET + "" + TextFormatting.BLUE + "% Speed");
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}
}