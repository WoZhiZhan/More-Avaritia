
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;
import net.wzz.more_avaritia.util.EventUtil;
import net.wzz.more_avaritia.util.RainbowText;

import javax.annotation.Nullable;
import java.util.List;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemSuperInfinitySword extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:super_infinity_sword")
	public static final Item block = null;

	public ItemSuperInfinitySword(ElementsMoreAvaritiaMod instance) {
		super(instance, 70);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:super_infinity_sword", "inventory");
		ModelLoader.registerItemVariants(ModItems.infinity_pickaxe, new ResourceLocation[]{sword});
		IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> (IBakedModel) modelRegistry.getObject(sword));
		ModelRegistryHelper.register(sword, wrapped);
		ModelLoader.setCustomMeshDefinition(block, (stack) -> sword);
	}

	public static class ItemCustom extends Item implements ICosmicRenderItem, IModelRegister {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("super_infinity_sword");
			setRegistryName("super_infinity_sword");
			setCreativeTab(Avaritia.tab);
			MinecraftForge.EVENT_BUS.register(this);
		}

		@SubscribeEvent
		public void onRightClickItem(PlayerInteractEvent.RightClickItem evt) {
			EntityPlayer player = evt.getEntityPlayer();
			if (player.getHeldItemMainhand().getItem() == new ItemStack(block).getItem()) {
				evt.setCancellationResult(EnumActionResult.SUCCESS);
				player.setActiveHand(evt.getHand());
			}
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public void onRenderLiving(RenderLivingEvent.Pre<AbstractClientPlayer> evt) {
			if ((evt.getEntity() instanceof AbstractClientPlayer)) {
				AbstractClientPlayer player = (AbstractClientPlayer)evt.getEntity();
				Minecraft mc = Minecraft.getMinecraft();
				if (player.isHandActive() && mc.player.getHeldItemMainhand().getItem() == new ItemStack(block).getItem()) {
					ModelPlayer model = (ModelPlayer)evt.getRenderer().getMainModel();
					boolean left1 = (player.getActiveHand() == EnumHand.OFF_HAND) && (player.getPrimaryHand() == EnumHandSide.RIGHT);
					boolean left2 = (player.getActiveHand() == EnumHand.MAIN_HAND) && (player.getPrimaryHand() == EnumHandSide.LEFT);
					if ((left1) || (left2)) {
						if (model.leftArmPose == ModelBiped.ArmPose.ITEM) {
							model.leftArmPose = ModelBiped.ArmPose.BLOCK;
						}
					} else if (model.rightArmPose == ModelBiped.ArmPose.ITEM) {
						model.rightArmPose = ModelBiped.ArmPose.BLOCK;
					}
				}
			}
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public void onRenderHand(RenderSpecificHandEvent evt) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null) && (player.isHandActive()) && (player.getActiveHand() == evt.getHand())) {
				ItemStack stack = evt.getItemStack();
				Minecraft mc = Minecraft.getMinecraft();
				if (mc.player.getHeldItemMainhand().getItem() == new ItemStack(block).getItem()) {
					GlStateManager.pushMatrix();
					boolean rightHanded = (evt.getHand() == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite()) == EnumHandSide.RIGHT;
					transformSideFirstPerson(rightHanded ? 1.0F : -1.0F, evt.getEquipProgress());
					Minecraft.getMinecraft().getItemRenderer().renderItemSide(player, stack, rightHanded ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightHanded);
					GlStateManager.popMatrix();
					evt.setCanceled(true);
				}
			}
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onItemTooltip(ItemTooltipEvent event) {
			if ((event.getItemStack().getItem() == block)) {
				List<String> tooltip = event.getToolTip();
				int size = tooltip.size();
				String attackDamage = I18n.format("attribute.name.generic.attackDamage");
				String replacementAttackDamage = new StringBuilder()
						.append(' ')
						.append(RainbowText.makeColour("Infinity"))
						.append(' ')
						.append(TextFormatting.GRAY.toString())
						.append(attackDamage).toString();
				for (int i = 0; i < size; i++) {
					String line = tooltip.get(i);
					if (line.contains(attackDamage)) {
						tooltip.set(i, replacementAttackDamage);
					}
				}
			}
		}

		private void transformSideFirstPerson(float side, float equippedProg) {
			GlStateManager.translate(side * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
			GlStateManager.translate(side * -0.1414214F, 0.08F, 0.1414214F);
			GlStateManager.rotate(-102.25F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(side * 13.365F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(side * 78.050003F, 0.0F, 0.0F, 1.0F);
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

		@SideOnly(Side.CLIENT)
		@Override
		public void registerModels() {
			ModelResourceLocation sword = new ModelResourceLocation("more_avaritia:super_infinity_sword", "inventory");
			ModelLoader.registerItemVariants(ModItems.infinity_pickaxe, new ResourceLocation[]{sword});
			IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, (modelRegistry) -> (IBakedModel) modelRegistry.getObject(sword));
			ModelRegistryHelper.register(sword, wrapped);
			ModelLoader.setCustomMeshDefinition(block, (stack) -> sword);
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

		@Override
		public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
			return true;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 3.4028235E+38F;
		}

		@Override
		public EnumAction getItemUseAction(ItemStack p_77661_1_) {
			return EnumAction.BLOCK;
		}

		@SideOnly(Side.CLIENT)
		public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
			return 1.0F;
		}

		@Override
		public int getItemEnchantability() {
			return 100;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack itemstack) {
			return 72000;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
			tooltip.add(I18n.format("item.super_infinity_sword.text1")+ RainbowText.makeColour(I18n.format("item.super_infinity_sword.text2"))+TextFormatting.GRAY+I18n.format("item.super_infinity_sword.text3"));
			tooltip.add(TextFormatting.YELLOW+I18n.format("item.super_infinity_sword.text4"));
			tooltip.add(TextFormatting.YELLOW+I18n.format("item.super_infinity_sword.text5"));
			tooltip.add(I18n.format("item.super_infinity_sword.text8")+ RainbowText.makeColour(I18n.format("item.super_infinity_sword.text6"))+TextFormatting.GRAY+I18n.format("item.super_infinity_sword.text7"));
			tooltip.add(I18n.format("item.super_infinity_sword.text9") + RainbowText.makeColour(I18n.format("item.super_infinity_sword.text10"))+TextFormatting.GRAY+I18n.format("item.super_infinity_sword.text11"));
		}

		@Override
		public boolean onEntitySwing(EntityLivingBase player, ItemStack p_onEntitySwing_2_) {
			Vec3d Vector = player.getLookVec();
			for (int i = 0; i < 20; i++) {
				double x = Vector.x * i;
				double y = player.getEyeHeight() + Vector.y * i;
				double z = Vector.z * i;
				List<Entity> Entities = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(2.0D, 2.0D, 2.0D).offset(x, y, z));
				if (!Entities.isEmpty()) {
					for (Entity entity : Entities) {
						if (entity instanceof EntityLivingBase) {
							if (!player.isSneaking())
								EventUtil.killEntityLiving((EntityLivingBase) entity, (EntityPlayer) player);
							else
								EventUtil.removeEntity((EntityLivingBase) entity);
						}
					}
				}
			}
			return super.onEntitySwing(player, p_onEntitySwing_2_);
		}

		@Override
		public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
			if (entity instanceof EntityLivingBase) {
				if (!player.isSneaking())
					EventUtil.killEntityLiving((EntityLivingBase) entity, player);
				else
					EventUtil.removeEntity((EntityLivingBase) entity);
			}
			return super.onLeftClickEntity(stack, player, entity);
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand p_77659_3_) {
			playerIn.setActiveHand(p_77659_3_);
			playerIn.setEntityInvulnerable(true);
			if (!world.loadedEntityList.contains(playerIn))
				world.loadedEntityList.add(playerIn);
			playerIn.isDead = false;
			if (playerIn.isSneaking()) {
				List list = playerIn.world.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().grow(12));
				for (Object item : list) {
					if ((item instanceof EntityItem)) {
						((EntityItem) item).posX = (playerIn).posX;
						((EntityItem) item).posZ = (playerIn).posZ;
						((EntityItem) item).posY = (playerIn).posY + 1;
						((EntityItem) item).onCollideWithPlayer(playerIn);
					} else if ((item instanceof EntityXPOrb)) {
						playerIn.xpCooldown = 0;
						((EntityXPOrb) item).onCollideWithPlayer(playerIn);
					}
				}
			}
			return super.onItemRightClick(world, playerIn, p_77659_3_);
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityLivingBase p_77615_3_, int p_77615_4_) {
			super.onPlayerStoppedUsing(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_);
			p_77615_3_.setEntityInvulnerable(false);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack p_77653_1_) {
			return RainbowText.makeColour(I18n.format("item.super_infinity_sword.name"));
		}
	}
}
