
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wzz.more_avaritia.init;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wzz.more_avaritia.entity.renderer.StarLightingRenderer;
import net.wzz.more_avaritia.entity.renderer.VoidRender;
import net.wzz.more_avaritia.entity.renderer.VoidSpeedRender;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MoreAvaritiaModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MoreAvaritiaModEntities.STAR_LIGHTING.get(), StarLightingRenderer::new);
		event.registerEntityRenderer(MoreAvaritiaModEntities.VOID_THROW.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MoreAvaritiaModEntities.VOID.get(), VoidRender::new);
		event.registerEntityRenderer(MoreAvaritiaModEntities.VOID_SPEED.get(), VoidSpeedRender::new);
	}
}
