/*
 *    MCreator note:
 *
 *    If you lock base mod element files, you can edit this file and it won't get overwritten.
 *    If you change your modid or package, you need to apply these changes to this file MANUALLY.
 *
 *    Settings in @Mod annotation WON'T be changed in case of the base mod element
 *    files lock too, so you need to set them manually here in such case.
 *
 *    If you do not lock base mod element files in Workspace settings, this file
 *    will be REGENERATED on each build.
 *
 */
package net.wzz.more_avaritia;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.wzz.more_avaritia.client.PlayerRender;
import net.wzz.more_avaritia.event.MoreClientInit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.init.MoreAvaritiaModFeatures;
import net.wzz.more_avaritia.init.MoreAvaritiaModBlocks;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;

@Mod("more_avaritia")
public class MoreAvaritiaMod {
	public static final Logger LOGGER = LogManager.getLogger(MoreAvaritiaMod.class);
	public static final String MODID = "more_avaritia";
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public MoreAvaritiaMod() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MoreClientInit.init();
		MoreAvaritiaModBlocks.REGISTRY.register(bus);
		MoreAvaritiaModItems.REGISTRY.register(bus);
		MoreAvaritiaModFeatures.REGISTRY.register(bus);
		MinecraftForge.EVENT_BUS.addListener(MoreClientInit::onRenderTick);
		bus.addListener(MoreAvaritiaMod::addLayers);
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MoreClientInit::init);
	}

	public static void addLayers(EntityRenderersEvent.AddLayers e) {
		addLayer(e, "default");
		addLayer(e, "slim");
	}

	static void addLayer(EntityRenderersEvent.AddLayers e, String s) {
		LivingEntityRenderer r = e.getSkin(s);
		r.addLayer(new PlayerRender(r));
	}

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}
}
