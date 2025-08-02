
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.ItemNBTUtils;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.tools.ItemPickaxeInfinity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;
import net.wzz.more_avaritia.util.MoreAvaritiaRegister;

import javax.annotation.Nullable;
import java.util.List;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemInfinityPickaxe extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_pickaxe")
	public static final Item block = null;
	public ItemInfinityPickaxe(ElementsMoreAvaritiaMod instance) {
		super(instance, 14);
	}

	@Override
	public void initElements() {
		elements.items.add(ItemCustom::new);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:infinity_pickaxe", "inventory");
		ModelResourceLocation hammer = new ModelResourceLocation(new ResourceLocation("more_avaritia:infinity_pickaxe2"), "infinity_pickaxe=hammer");
		ModelLoader.registerItemVariants(block, sword, hammer);
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> modelRegistry.getObject(sword));
		ModelRegistryHelper.register(sword, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> (stack.hasTagCompound() && ItemNBTUtils.getBoolean(stack, "hammer")) ? hammer : sword);
	}
	public static class ItemCustom extends ItemPickaxeInfinity implements ICosmicRenderItem, IModelRegister {
		public static boolean mode = false;
		public static int modeInt = 0;
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("infinity_pickaxe");
			setCreativeTab(Avaritia.tab);
		}
		public EnumRarity getRarity(ItemStack stack) {
			return ModItems.COSMIC_RARITY;
		}
		public Entity createEntity(World world, Entity location, ItemStack itemstack) {
			return new EntityImmortalItem(world, location, itemstack);
		}
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
			return MoreAvaritiaRegister.PICKAXE;
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
			if (textureMode != 0) {
				return super.getItemStackDisplayName(stack)+"·锤子模式";
			}
			return super.getItemStackDisplayName(stack);
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			if (modeInt == 0) {
				return 128000000;
			}
			return super.getDestroySpeed(stack, state);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add(I18n.format("item.infinity_pickaxe.text1"));
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
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) < 10) {
					stack.addEnchantment(Enchantments.FORTUNE, 10);
				}
				if (mode && modeInt == 1) {
					mode = false;
					modeInt = 0;
				}
				if (!mode && modeInt == 0) {
					mode = true;
					modeInt = 1;
				}
				tags.setBoolean("hammer", !tags.getBoolean("hammer"));
				player.swingArm(hand);
				return new ActionResult(EnumActionResult.SUCCESS, stack);
			} else {
				return new ActionResult(EnumActionResult.PASS, stack);
			}
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean hasEffect(ItemStack itemstack) {
			return true;
		}

		@Override
		public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
			if (entityLiving instanceof EntityPlayer) {
				if (!world.isRemote) {
					Block block = state.getBlock();
					if (block instanceof BlockOre) {
						ItemStack resultStack = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(block, 1, block.getMetaFromState(state)));
						if (resultStack.isEmpty()) {
							NonNullList<ItemStack> matchingStacks = OreDictionary.getOres("ore" + block.getRegistryName().getResourcePath());
							for (ItemStack matchingStack : matchingStacks) {
								resultStack = FurnaceRecipes.instance().getSmeltingResult(matchingStack);
								if (!resultStack.isEmpty()) {
									break;
								}
							}
						}
						if (!resultStack.isEmpty()) {
							int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
							int oreCount = block.quantityDropped(state, fortuneLevel, world.rand);
							int exp = block.getExpDrop(state, world, pos, fortuneLevel);
							dropItemInWorld(world, pos, resultStack, oreCount, exp);
							world.setBlockToAir(pos);
							return true;
						}
					}
				}
			}
			return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
		}

		public static void dropItemInWorld(World world, BlockPos pos, ItemStack stack, int count, int exp) {
			for (int i = 0; i < count; i++) {
				EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
				entityItem.motionX = world.rand.nextFloat() * 0.2F - 0.1F;
				entityItem.motionY = world.rand.nextFloat() * 0.2F + 0.1F;
				entityItem.motionZ = world.rand.nextFloat() * 0.2F - 0.1F;
				world.spawnEntity(entityItem);
			}
			if (exp > 0) {
				world.spawnEntity(new EntityXPOrb(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp));
			}
		}
	}
}
