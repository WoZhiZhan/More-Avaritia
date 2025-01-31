
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.model.bakery.ModelBakery;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.client.render.item.InfinityBowModelWrapper;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.item.tools.ItemBowInfinity;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemInfinityBow extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_bow")
	public static final Item block = null;
	public ItemInfinityBow(ElementsMoreAvaritiaMod instance) {
		super(instance, 18);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelBakery.registerItemKeyGenerator(block, (stack) -> {
			String key = ModelBakery.defaultItemKeyGenerator.generateKey(stack);
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("frame")) {
				key = key + "@pull=" + stack.getTagCompound().getInteger("frame");
			}
			return key;
		});
		ModelResourceLocation location = new ModelResourceLocation("more_avaritia:idle");
		IBakedModel actualModel = new InfinityBowModelWrapper();
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_BOW, (modelRegistry) -> {
			return actualModel;
		});
		ModelRegistryHelper.register(location, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> {
			return location;
		});
	}
	public static class ItemCustom extends ItemBowInfinity {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("infinity_bow");
			setCreativeTab(Avaritia.tab);
		}

		public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
			if (count == 1) {
				this.fire(stack, player.world, player, 0);
			}
			fire(stack, player.world, player, 0);
		}

		public void fire(ItemStack stack, World world, EntityLivingBase player, int useCount) {
			int max = this.getMaxItemUseDuration(stack);
			float maxf = (float)max;
			int j = max - useCount;
			float f = (float)j / maxf;
			f = (f * f + f * 2.0F) / 3.0F;
			if (!((double)f < 0.1)) {
				if ((double)f > 1.0) {
					f = 1.0F;
				}
				EntityArrow arrow = new EntityHeavenArrow(world, player);
				arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
				arrow.setDamage(100.0);
				if (f == 1.0F) {
					arrow.setIsCritical(true);
				}
				int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
				if (k > 0) {
					arrow.setDamage(arrow.getDamage() + (double)k + 1.0);
				}
				int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
				if (l > 0) {
					arrow.setKnockbackStrength(l);
				}
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
					arrow.setFire(100);
				}
				world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
				arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
				EntityArrow arrow2 = new EntityHeavenArrow(world, player);
				arrow2.shoot(player, player.rotationPitch + new Random().nextInt(10), player.rotationYaw + new Random().nextInt(10), 0.0F, f * 3.0F, 1.0F);
				arrow2.setDamage(100.0);
				if (!world.isRemote) {
					world.spawnEntity(arrow);
					world.spawnEntity(arrow2);
				}
			}
		}

		@Override
		public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
			super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
			EntityPlayer player = (EntityPlayer) entityIn;
			if (player.getHeldItemMainhand().getItem() == block) {
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,10,3));
			}
		}

		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add(I18n.format("item.infinity_bow.text1"));
		}
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
			int frame = -1;
			if (player != null) {
				int bframe = InfinityBowModelWrapper.getBowFrame(player);
				frame = bframe != 0 ? bframe : -1;
			}

			return frame == -1 ? AvaritiaTextures.INFINITY_BOW_IDLE_MASK : AvaritiaTextures.INFINITY_BOW_PULL_MASK[frame];
		}

		@SideOnly(Side.CLIENT)
		public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
			return 1.0F;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean hasEffect(ItemStack itemstack) {
			return true;
		}
	}
}
