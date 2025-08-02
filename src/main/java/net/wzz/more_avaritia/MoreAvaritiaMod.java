package net.wzz.more_avaritia;

import codechicken.lib.texture.TextureUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.more_avaritia.item.*;
import net.wzz.more_avaritia.util.HasItemFromSlot;
import net.wzz.more_avaritia.util.ModelArmorSuperInfinity;
import net.wzz.more_avaritia.util.MoreAvaritiaList;
import net.wzz.more_avaritia.util.MoreAvaritiaRegister;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

import static net.minecraft.client.Minecraft.getSystemTime;
import static net.wzz.more_avaritia.util.MoreAvaritiaRegister.*;

@Mod(modid = MoreAvaritiaMod.MODID, version = MoreAvaritiaMod.VERSION,dependencies = "required-after:codechickenlib;required-after:avaritia")
public class MoreAvaritiaMod {
	public static final String MODID = "more_avaritia";
	public static final String VERSION = "1.0";
	public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("more_avaritia:a");
	@SidedProxy(clientSide = "net.wzz.more_avaritia.ClientProxyMoreAvaritiaMod", serverSide = "net.wzz.more_avaritia.ServerProxyMoreAvaritiaMod")
	public static IProxyMoreAvaritiaMod proxy;
	@Mod.Instance(MODID)
	public static MoreAvaritiaMod instance;
	public ElementsMoreAvaritiaMod elements = new ElementsMoreAvaritiaMod();
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.registerWorldGenerator(elements, 5);
		GameRegistry.registerFuelHandler(elements);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ElementsMoreAvaritiaMod.GuiHandler());
		elements.preInit(event);
		MinecraftForge.EVENT_BUS.register(elements);
		elements.getElements().forEach(element -> element.preInit(event));
		proxy.preInit(event);
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			TextureUtils.addIconRegister(new MoreAvaritiaRegister());
		}
	}

	@SubscribeEvent
	public void Tick(TickEvent.PlayerTickEvent e) {
		if (HasItemFromSlot.hasArmor(e.player)) {
			e.player.setHealth(e.player.getMaxHealth());
			e.player.isDead = false;
			e.player.deathTime = 0;
			e.player.setFire(0);
			e.player.setInvisible(false);
			e.player.capabilities.allowFlying = true;
			e.player.sendPlayerAbilities();
			MoreAvaritiaList.addFly(e.player);
		} else if (!e.player.isCreative() && MoreAvaritiaList.isFly(e.player)) {
			e.player.capabilities.allowFlying = false;
			e.player.capabilities.isFlying = false;
			e.player.sendPlayerAbilities();
			MoreAvaritiaList.removeFly(e.player);
		}
	}

	@SubscribeEvent
	public void onHurt(LivingHurtEvent e) {
		if (HasItemFromSlot.hasArmor(e.getEntityLiving())) {
			e.setCanceled(true);
		}
		if (e.getAmount() >= e.getEntityLiving().getHealth() && e.getEntityLiving() instanceof EntityPlayer) {
			if (((EntityPlayer) e.getEntityLiving()).inventory.hasItemStack(new ItemStack(ItemInfinityTotem.block))) {
				e.setCanceled(true);
				((EntityPlayer) e.getEntityLiving()).inventory.clearMatchingItems(ItemInfinityTotem.block,-1,1,null);
				e.getEntityLiving().heal(e.getAmount());
				e.getEntityLiving().isDead = false;
				e.getEntityLiving().deathTime = 0;
				e.getEntityLiving().playSound(SoundEvents.ITEM_TOTEM_USE,1,1);
				e.getEntityLiving().playSound(SoundEvents.ENTITY_PLAYER_LEVELUP,1,1);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void colorItem(ColorHandlerEvent.Item event) {
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 1000f) % 1, .8f, 1), ItemEndlessEnergy.block);
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 5000f) % 1, .8f, 1.5f), ItemChargingCrystal.block);
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 2000f) % 1, .8f, 1.5f), ItemInfinityArmor.body);
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 2000f) % 1, .8f, 1.5f), ItemInfinityArmor.boots);
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 2000f) % 1, .8f, 1.5f), ItemInfinityArmor.helmet);
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 2000f) % 1, .8f, 1.5f), ItemInfinityArmor.legs);
		event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> MathHelper.hsvToRGB((getSystemTime() / 8000f) % 1, .8f, 1), ItemSuperInfinitySword.block);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (HasItemFromSlot.hasArmor(player)) {
				if (e.getGui() instanceof GuiGameOver)
					e.setCanceled(true);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void textureStichPost(TextureStitchEvent.Post event) {
		TextureUtils.bindBlockTexture();
		ModelArmorSuperInfinity.itempagewidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
		ModelArmorSuperInfinity.itempageheight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
		ModelArmorSuperInfinity.armorModel.rebuildOverlay();
		ModelArmorSuperInfinity.legModel.rebuildOverlay();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		elements.getElements().forEach(element -> element.init(event));
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		elements.getElements().forEach(element -> element.serverLoad(event));
		proxy.serverLoad(event);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(elements.getBlocks().stream().map(Supplier::get).toArray(Block[]::new));
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(elements.getItems().stream().map(Supplier::get).toArray(Item[]::new));
		init();
	}

	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event) {
		event.getRegistry().registerAll(elements.getBiomes().stream().map(Supplier::get).toArray(Biome[]::new));
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().registerAll(elements.getEntities().stream().map(Supplier::get).toArray(EntityEntry[]::new));
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll(elements.getPotions().stream().map(Supplier::get).toArray(Potion[]::new));
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
		elements.registerSounds(event);
	}

	public static ItemArmor infinity_helmet = new ItemSuperInfinityArmor.Helmet();

	public static ItemArmor infinity_chestplate = new ItemSuperInfinityArmor.Chestplate();

	public static ItemArmor infinity_pants = new ItemSuperInfinityArmor.Legs();

	public static ItemArmor infinity_boots = new ItemSuperInfinityArmor.Boots();

	public static void init() {
		infinity_helmet = new ItemSuperInfinityArmor.Helmet();
		ForgeRegistries.ITEMS.register(infinity_helmet.setRegistryName("more_avaritia", "super_infinity_helmet"));
		infinity_chestplate = new ItemSuperInfinityArmor.Chestplate();
		ForgeRegistries.ITEMS.register(infinity_chestplate.setRegistryName("more_avaritia", "super_infinity_chestplate"));
		infinity_pants = new ItemSuperInfinityArmor.Legs();
		ForgeRegistries.ITEMS.register(infinity_pants.setRegistryName("more_avaritia", "super_infinity_pants"));
		infinity_boots = new ItemSuperInfinityArmor.Boots();
		infinity_boots.setUnlocalizedName("super_infinity_boots");
		ForgeRegistries.ITEMS.register(infinity_boots.setRegistryName("more_avaritia", "super_infinity_boots"));
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			initClient();
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		elements.getElements().forEach(element -> element.registerModels(event));
	}
	static {
		FluidRegistry.enableUniversalBucket();
	}
}
