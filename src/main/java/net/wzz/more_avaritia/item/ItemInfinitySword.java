
package net.wzz.more_avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import com.google.common.collect.Multimap;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.tools.ItemSwordInfinity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;
import net.wzz.more_avaritia.util.RainbowText;

import javax.annotation.Nullable;
import java.util.List;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemInfinitySword extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_sword")
	public static final Item block = null;
	public ItemInfinitySword(ElementsMoreAvaritiaMod instance) {
		super(instance, 13);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:infinity_sword", "inventory");
		ModelLoader.registerItemVariants(block, new ResourceLocation[]{sword});
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> (IBakedModel) modelRegistry.getObject(sword));
		ModelRegistryHelper.register(sword, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> sword);
	}
	public static class ItemCustom extends ItemSwordInfinity implements ICosmicRenderItem, IModelRegister {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("infinity_sword");
			setCreativeTab(Avaritia.tab);
		}

		@Override
		public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
			Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
			if (slot == EntityEquipmentSlot.MAINHAND) {
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
						new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Item modifier", Double.POSITIVE_INFINITY, 0));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Item modifier", -2.4, 0));
			}
			return multimap;
		}

		@Override
		public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
			if (entity instanceof EntityLivingBase) {
				entity.setFire(100);
				entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) Double.POSITIVE_INFINITY);
				((EntityLivingBase) entity).onDeath(DamageSource.OUT_OF_WORLD);
				((EntityLivingBase) entity).setHealth(0);
				if (((EntityLivingBase) entity).getHealth() > 0 && !(entity instanceof EntityPlayer)) {
					entity.isDead = true;
					entity.world.loadedEntityList.remove(entity);
					entity.updateBlocked = true;
					((EntityLivingBase) entity).deathTime = 20;
					entity.world.weatherEffects.remove(entity);
					entity.setInvisible(true);
					player.sendStatusMessage(new TextComponentString("检测到生物未死亡已强制抹除"), true);
				}
			}
			return super.onLeftClickEntity(stack, player, entity);
		}

		@Override
		public int getItemEnchantability() {
			return 100;
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

		@SideOnly(Side.CLIENT)
		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add(I18n.format("item.infinity_sword.text1"));
			tooltip.add(RainbowText.makeColour(I18n.format("item.infinity_sword.text2")));
		}

		public Entity createEntity(World world, Entity location, ItemStack itemstack) {
			return new EntityImmortalItem(world, location, itemstack);
		}

		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
			return AvaritiaTextures.INFINITY_SWORD_MASK;
		}

		public EnumRarity getRarity(ItemStack stack) {
			return ModItems.COSMIC_RARITY;
		}

		@SideOnly(Side.CLIENT)
		public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
			return 1.0F;
		}
	}
}
