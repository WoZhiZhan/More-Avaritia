
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wzz.more_avaritia.init;

import committee.nova.mods.avaritia.init.registry.ModCreativeModeTabs;
import net.wzz.more_avaritia.MoreAvaritiaMod;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoreAvaritiaModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreAvaritiaMod.MODID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey().equals(ModCreativeModeTabs.CREATIVE_TAB.getKey())) {
			tabData.accept(MoreAvaritiaModItems.NEUTRON_HELMET.get());
			tabData.accept(MoreAvaritiaModItems.NEUTRON_CHESTPLATE.get());
			tabData.accept(MoreAvaritiaModItems.NEUTRON_LEGGINGS.get());
			tabData.accept(MoreAvaritiaModItems.NEUTRON_BOOTS.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_FAKE_HELMET.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_FAKE_CHESTPLATE.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_FAKE_LEGGINGS.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_FAKE_BOOTS.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_SWORD.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_PICKAXE.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_AXE.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_SHOVEL.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_HOE.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_BOW.get());
			tabData.accept(MoreAvaritiaModItems.NEUTRON_BOW.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_GOD_SWORD.get());
			tabData.accept(MoreAvaritiaModItems.COMPRESSED_INFINITY_INGOT.get());
			tabData.accept(MoreAvaritiaModItems.COMPRESSED_INFINITY.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_APPLE.get());
			tabData.accept(MoreAvaritiaModItems.UNIVERSE_HEART.get());
			tabData.accept(MoreAvaritiaModItems.ROD_RULING.get());
			tabData.accept(MoreAvaritiaModItems.RESOUND_SWORD.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_HEAD.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_Chestplate.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_LEGS.get());
			tabData.accept(MoreAvaritiaModItems.INFINITY_BOOTS.get());
		}
	}
}
