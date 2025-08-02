
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.tools.ItemAxeInfinity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.wzz.more_avaritia.util.MoreAvaritiaRegister;

import javax.annotation.Nullable;
import java.util.List;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemInfinityAxe extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_axe")
	public static final Item block = null;
	public ItemInfinityAxe(ElementsMoreAvaritiaMod instance) {
		super(instance, 17);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:infinity_axe", "inventory");
		ModelLoader.registerItemVariants(block, sword);
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> (IBakedModel) modelRegistry.getObject(sword));
		ModelRegistryHelper.register(sword, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> sword);
	}
	public static class ItemCustom extends ItemAxeInfinity implements ICosmicRenderItem, IModelRegister {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("infinity_axe");
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
			return MoreAvaritiaRegister.AXE;
		}

		@SideOnly(Side.CLIENT)
		public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
			return 0.5F;
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			return super.getDestroySpeed(stack, state) + 10;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add(I18n.format("item.infinity_axe.text1"));
		}

		@Override
		public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
			if (!world.isRemote && state.getBlockHardness(world, pos) != 0.0D) {
				List<ItemStack> drops = state.getBlock().getDrops(world, pos, state, 0);
				for (int i = 0; i < drops.size(); i++) {
					ItemStack drop = drops.get(i).copy();
					drop.setCount(drop.getCount() * 2);
					drops.set(i, drop);
				}
				for (ItemStack drop : drops) {
					Block.spawnAsEntity(world, pos, drop);
				}
			}
			return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean hasEffect(ItemStack itemstack) {
			return true;
		}

		@Override
		public void registerModels() {

		}
	}
}
