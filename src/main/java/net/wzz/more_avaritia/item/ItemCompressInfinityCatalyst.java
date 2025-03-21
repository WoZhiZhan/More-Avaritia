
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;
import net.wzz.more_avaritia.util.MoreAvaritiaRegister;
import net.wzz.more_avaritia.util.RainbowText;
import net.wzz.more_avaritia.util.RainbowFont;

import javax.annotation.Nullable;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemCompressInfinityCatalyst extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:compress_infinity_catalyst")
	public static final Item block = null;
	public ItemCompressInfinityCatalyst(ElementsMoreAvaritiaMod instance) {
		super(instance, 61);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:compress_infinity_catalyst", "inventory");
		ModelLoader.registerItemVariants(block, sword);
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> (IBakedModel) modelRegistry.getObject(sword));
		ModelRegistryHelper.register(sword, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> sword);
	}
	public static class ItemCustom extends Item implements ICosmicRenderItem, IModelRegister {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 64;
			setUnlocalizedName("compress_infinity_catalyst");
			setRegistryName("compress_infinity_catalyst");
			setCreativeTab(Avaritia.tab);
		}
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
			return MoreAvaritiaRegister.INFINITY_CATALYST;
		}

		@SideOnly(Side.CLIENT)
		public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
			return 1.0F;
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
		public String getItemStackDisplayName(ItemStack p_77653_1_) {
			return RainbowText.makeColour(super.getItemStackDisplayName(p_77653_1_));
		}

		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add("无穷无尽的力量。。。");
		}

		@Nullable
		@Override
		public FontRenderer getFontRenderer(ItemStack p_getFontRenderer_1_) {
			return RainbowFont.Instance;
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
