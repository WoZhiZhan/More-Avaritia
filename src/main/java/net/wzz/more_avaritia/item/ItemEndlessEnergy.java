
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.client.render.item.HaloRenderItem;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import java.util.List;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemEndlessEnergy extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:endless_energy")
	public static final Item block = null;
	public ItemEndlessEnergy(ElementsMoreAvaritiaMod instance) {
		super(instance, 3);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("more_avaritia:endless_energy", "inventory"));
		IBakedModel wrappedModel = new HaloRenderItem(TransformUtils.DEFAULT_ITEM, (modelRegistry) -> (IBakedModel)modelRegistry.getObject(new ModelResourceLocation("more_avaritia:endless_energy")));
		ModelRegistryHelper.register(new ModelResourceLocation("more_avaritia:endless_energy"), wrappedModel);
	}
	public static class ItemCustom extends Item {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 64;
			setUnlocalizedName("endless_energy");
			setRegistryName("endless_energy");
			setCreativeTab(Avaritia.tab);
		}

		@SideOnly(Side.CLIENT)
		public boolean shouldDrawHalo(ItemStack stack) {
			return true;
		}

		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getHaloTexture(ItemStack stack) {
			return AvaritiaTextures.HALO;
		}

		@SideOnly(Side.CLIENT)
		public int getHaloSize(ItemStack stack) {
			return 4;
		}

		@SideOnly(Side.CLIENT)
		public int getHaloColour(ItemStack stack) {
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
		public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
			super.addInformation(itemstack, world, list, flag);
			list.add(I18n.format("item.endless_energy.text1"));
		}
	}
}
