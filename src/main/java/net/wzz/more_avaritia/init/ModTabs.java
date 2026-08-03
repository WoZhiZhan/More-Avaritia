
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

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreAvaritiaMod.MODID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey().equals(ModCreativeModeTabs.CREATIVE_TAB.getKey())) {
			tabData.accept(ModItems.NEUTRON_HELMET.get());
			tabData.accept(ModItems.NEUTRON_CHESTPLATE.get());
			tabData.accept(ModItems.NEUTRON_LEGGINGS.get());
			tabData.accept(ModItems.NEUTRON_BOOTS.get());
			tabData.accept(ModItems.INFINITY_FAKE_HELMET.get());
			tabData.accept(ModItems.INFINITY_FAKE_CHESTPLATE.get());
			tabData.accept(ModItems.INFINITY_FAKE_LEGGINGS.get());
			tabData.accept(ModItems.INFINITY_FAKE_BOOTS.get());
			tabData.accept(ModItems.INFINITY_SWORD.get());
			tabData.accept(ModItems.INFINITY_PICKAXE.get());
			tabData.accept(ModItems.INFINITY_AXE.get());
			tabData.accept(ModItems.INFINITY_SHOVEL.get());
			tabData.accept(ModItems.INFINITY_HOE.get());
			tabData.accept(ModItems.INFINITY_BOW.get());
			tabData.accept(ModItems.NEUTRON_BOW.get());
			tabData.accept(ModItems.INFINITY_GOD_SWORD.get());
			tabData.accept(ModItems.COMPRESSED_INFINITY_INGOT.get());
			tabData.accept(ModItems.COMPRESSED_INFINITY.get());
			tabData.accept(ModItems.INFINITY_APPLE.get());
			tabData.accept(ModItems.UNIVERSE_HEART.get());
			tabData.accept(ModItems.ROD_RULING.get());
			tabData.accept(ModItems.RESOUND_SWORD.get());
			tabData.accept(ModItems.INFINITY_HEAD.get());
			tabData.accept(ModItems.INFINITY_Chestplate.get());
			tabData.accept(ModItems.INFINITY_LEGS.get());
			tabData.accept(ModItems.INFINITY_BOOTS.get());
			tabData.accept(ModItems.LIGHTING_STAFF.get());
		}
	}
}
