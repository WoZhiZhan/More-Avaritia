
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.HaloRenderItem;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
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

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemCompressedInfinityIngot extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:compressed_infinity_ingot")
	public static final Item block = null;
	public ItemCompressedInfinityIngot(ElementsMoreAvaritiaMod instance) {
		super(instance, 19);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation location = new ModelResourceLocation("more_avaritia:compressed_infinity_ingot", "inventory");
		ModelLoader.registerItemVariants((Item)block, new ResourceLocation[] { (ResourceLocation)location });
		HaloRenderItem haloRenderItem = new HaloRenderItem((IModelState) TransformUtils.DEFAULT_ITEM, modelRegistry -> (IBakedModel)modelRegistry.getObject(location));
		ModelRegistryHelper.register(location, (IBakedModel)haloRenderItem);
		ModelLoader.setCustomMeshDefinition((Item)block, stack -> location);
	}
	public static class ItemCustom extends Item implements IHaloRenderItem, IModelRegister {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 64;
			setUnlocalizedName("compressed_infinity_ingot");
			setRegistryName("compressed_infinity_ingot");
			setCreativeTab(Avaritia.tab);
		}

		@SideOnly(Side.CLIENT)
		public boolean shouldDrawHalo(ItemStack stack) {
			return true;
		}

		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getHaloTexture(ItemStack stack) {
			return AvaritiaTextures.HALO_NOISE;
		}

		@SideOnly(Side.CLIENT)
		public int getHaloSize(ItemStack stack) {
			return 8;
		}

		@SideOnly(Side.CLIENT)
		public boolean shouldDrawPulse(ItemStack stack) {
			return true;
		}

		@SideOnly(Side.CLIENT)
		public int getHaloColour(ItemStack stack) {
			int meta = stack.getItemDamage();
			if (meta == 2)
				return 872415231;
			if (meta == 3)
				return 1308622847;
			if (meta == 4)
				return -1711276033;
			return -16777216;
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 1F;
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
