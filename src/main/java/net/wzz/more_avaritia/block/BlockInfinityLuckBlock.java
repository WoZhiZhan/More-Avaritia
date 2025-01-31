
package net.wzz.more_avaritia.block;

import morph.avaritia.Avaritia;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class BlockInfinityLuckBlock extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_luck_block")
	public static final Block block = null;
	public BlockInfinityLuckBlock(ElementsMoreAvaritiaMod instance) {
		super(instance, 25);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new BlockCustom().setRegistryName("infinity_luck_block"));
		elements.items.add(() -> new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation("more_avaritia:infinity_luck_block", "inventory"));
	}
	public static class BlockCustom extends Block {
		public BlockCustom() {
			super(Material.ROCK);
			setUnlocalizedName("infinity_luck_block");
			setSoundType(SoundType.STONE);
			setHardness(0.1F);
			setResistance(10F);
			setLightLevel(0F);
			setLightOpacity(255);
			setCreativeTab(Avaritia.tab);
		}

		@Override
		public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
			drops.add(new ItemStack(Blocks.AIR, (int) (1)));
		}

		@Override
		public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer entity, boolean willHarvest) {
			boolean retval = super.removedByPlayer(state, world, pos, entity, willHarvest);
				Random random = new Random();
				List<Item> items = new ArrayList<>();
				for (ResourceLocation rl : ForgeRegistries.ITEMS.getKeys()) {
					Item item = ForgeRegistries.ITEMS.getValue(rl);
					if (item != null && item.getClass().getName().startsWith("morph.avaritia") ||
							item.getClass().getName().startsWith("net.wzz.more_avaritia")) {
						items.add(item);
					}
				}
				if (!items.isEmpty()) {
					Item selected = items.get(random.nextInt(items.size()));
					int count = 1;
					ItemStack stack = new ItemStack(selected, count);
					if (selected instanceof ItemBlock) {
						ItemBlock blockItem = (ItemBlock) selected;
						NonNullList<ItemStack> subItems = NonNullList.create();
						if (!subItems.isEmpty()) {
							blockItem.getBlock().getDrops(subItems, world, pos, world.getBlockState(pos), 0);
							ItemStack subStack = subItems.get(random.nextInt(subItems.size()));
							stack.setTagCompound(subStack.getTagCompound());
						}
					}
					if (!world.isRemote) {
						EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
						world.spawnEntity(entityItem);
					}
				}
			return retval;
		}
	}
}
