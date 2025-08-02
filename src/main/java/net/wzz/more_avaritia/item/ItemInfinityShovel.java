
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.tools.ItemShovelInfinity;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;
import net.wzz.more_avaritia.util.MoreAvaritiaRegister;

import javax.annotation.Nullable;
import java.util.List;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemInfinityShovel extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_shovel")
	public static final Item block = null;
	public ItemInfinityShovel(ElementsMoreAvaritiaMod instance) {
		super(instance, 15);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:infinity_shovel", "inventory");
		ModelLoader.registerItemVariants(block, sword);
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> (IBakedModel) modelRegistry.getObject(sword));
		ModelRegistryHelper.register(sword, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> sword);
	}
	public static class ItemCustom extends ItemShovelInfinity implements ICosmicRenderItem, IModelRegister {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("infinity_shovel");
			setCreativeTab(Avaritia.tab);
		}
		public EnumRarity getRarity(ItemStack stack) {
			return ModItems.COSMIC_RARITY;
		}
		public Entity createEntity(World world, Entity location, ItemStack itemstack) {
			return (Entity)new EntityImmortalItem(world, location, itemstack);
		}
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
			return MoreAvaritiaRegister.SHOVEL;
		}

		@SideOnly(Side.CLIENT)
		public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
			return 0.5F;
		}
		@Override
		public void registerModels() {

		}
		private int textureMode = 0;

		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			if (stack.getTagCompound() != null) {
				textureMode = stack.getTagCompound().getCompoundTag("texture").getInteger("mode");
			}
			if (textureMode == 1) {
				return super.getItemStackDisplayName(stack)+"·挖掘模式";
			}
			return super.getItemStackDisplayName(stack);
		}

		@Override
		public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
			super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
			EntityPlayer player = (EntityPlayer) entityIn;
			if (player.getHeldItemMainhand().getItem() == block) {
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,10,4));
			}
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add(I18n.format("item.infinity_shovel.text1"));
		}

		public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
			ItemStack stack = player.getHeldItem(hand);
			if (player.isSneaking()) {
				NBTTagCompound tags = stack.getTagCompound();
				if (!world.isRemote) {
					NBTTagCompound nbt = stack.getOrCreateSubCompound("texture");
					int mode = nbt.getInteger("mode");
					if (mode == 0) {
						mode = 1;
					} else {
						mode = 0;
					}
					nbt.setInteger("mode", mode);
				}
				if (tags == null) {
					tags = new NBTTagCompound();
					stack.setTagCompound(tags);
				}
				tags.setBoolean("destroyer", !tags.getBoolean("destroyer"));
				player.swingArm(hand);
			}
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean hasEffect(ItemStack itemstack) {
			return true;
		}
	}
}
