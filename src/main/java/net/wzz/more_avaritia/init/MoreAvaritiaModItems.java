
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wzz.more_avaritia.init;

import morph.avaritia.init.AvaritiaModContent;
import net.wzz.more_avaritia.item.*;
import net.wzz.more_avaritia.MoreAvaritiaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;

public class MoreAvaritiaModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MoreAvaritiaMod.MODID);
	public static final RegistryObject<Item> COMPRESSED_AXE = REGISTRY.register("compressed_axe", () -> new CompressedAxeItem());
	public static final RegistryObject<Item> COMPRESSED_PICKAXE = REGISTRY.register("compressed_pickaxe", () -> new CompressedPickaxeItem());
	public static final RegistryObject<Item> COMPRESSED_SWORD = REGISTRY.register("compressed_sword", () -> new CompressedSwordItem());
	public static final RegistryObject<Item> COMPRESSED_SHOVEL = REGISTRY.register("compressed_shovel", () -> new CompressedShovelItem());
	public static final RegistryObject<Item> COMPRESSED_HOE = REGISTRY.register("compressed_hoe", () -> new CompressedHoeItem());
	public static final RegistryObject<Item> COMPRESSED_NEUTRON_INGOT = REGISTRY.register("compressed_neutron_ingot", () -> new CompressedNeutronIngotItem());
	public static final RegistryObject<Item> NEUTRON_STICK = REGISTRY.register("neutron_stick", () -> new NeutronStickItem());
	public static final RegistryObject<Item> NEUTRON_ARMOR_HELMET = REGISTRY.register("neutron_armor_helmet", () -> new NeutronArmorItem.Helmet());
	public static final RegistryObject<Item> NEUTRON_ARMOR_CHESTPLATE = REGISTRY.register("neutron_armor_chestplate", () -> new NeutronArmorItem.Chestplate());
	public static final RegistryObject<Item> NEUTRON_ARMOR_LEGGINGS = REGISTRY.register("neutron_armor_leggings", () -> new NeutronArmorItem.Leggings());
	public static final RegistryObject<Item> NEUTRON_ARMOR_BOOTS = REGISTRY.register("neutron_armor_boots", () -> new NeutronArmorItem.Boots());
	public static final RegistryObject<Item> COMPRESSED_INFINITY_INGOT = REGISTRY.register("compressed_infinity_ingot", () -> new CompressedInfinityIngotItem());
	public static final RegistryObject<Item> COMPRESSED_NEUTRON_BLOCK = block(MoreAvaritiaModBlocks.COMPRESSED_NEUTRON_BLOCK, AvaritiaModContent.TAB);
	public static final RegistryObject<Item> COMPRESSED_INFINITY_BLOCK = block(MoreAvaritiaModBlocks.COMPRESSED_INFINITY_BLOCK, AvaritiaModContent.TAB);
	public static final RegistryObject<Item> INFINITY_SWORD = REGISTRY.register("infinity_sword", () -> new InfinitySwordItem());
	public static final RegistryObject<Item> ENDLESS_ENERGY = REGISTRY.register("endless_energy", () -> new EndlessEnergyItem());
	public static final RegistryObject<Item> COAL_SINGULARITY = REGISTRY.register("coal_singularity", () -> new CoalSingularityItem());
	public static final RegistryObject<Item> INFINITY_SINGULARITY = REGISTRY.register("infinity_singularity", () -> new InfinitySingularityItem());
	public static final RegistryObject<Item> INFINITY_ARMOR_ARMOR_HELMET = REGISTRY.register("infinity_armor_armor_helmet", () -> new Infinity_armor_ArmorItem.Helmet());
	public static final RegistryObject<Item> INFINITY_ARMOR_ARMOR_CHESTPLATE = REGISTRY.register("infinity_armor_armor_chestplate", () -> new Infinity_armor_ArmorItem.Chestplate());
	public static final RegistryObject<Item> INFINITY_ARMOR_ARMOR_LEGGINGS = REGISTRY.register("infinity_armor_armor_leggings", () -> new Infinity_armor_ArmorItem.Leggings());
	public static final RegistryObject<Item> INFINITY_ARMOR_ARMOR_BOOTS = REGISTRY.register("infinity_armor_armor_boots", () -> new Infinity_armor_ArmorItem.Boots());
	public static final RegistryObject<Item> CHARGING_CRYSTAL_ORE = block(MoreAvaritiaModBlocks.CHARGING_CRYSTAL_ORE, AvaritiaModContent.TAB);
	public static final RegistryObject<Item> CHARGING_CRYSTAL = REGISTRY.register("charging_crystal", () -> new ChargingCrystalItem());
	public static final RegistryObject<Item> INFINITY_BOW = REGISTRY.register("infinity_bow", () -> new InfinityBowItem());
	public static final RegistryObject<Item> INFINITY_APPLE = REGISTRY.register("infinity_apple", () -> new InfinityAppleItem());
	public static final RegistryObject<Item> SUPER_INFINITY_CATALYST = REGISTRY.register("super_infinity_catalyst", () -> new SuperInfinityCatalystItem());
	public static final RegistryObject<Item> INFINITYTOTEM = REGISTRY.register("infinitytotem", () -> new InfinitytotemItem());
	public static final RegistryObject<Item> INFINITY_SWORD_GOD = REGISTRY.register("infinity_sword_god", () -> new InfinitySwordGodItem());
	public static final RegistryObject<Item> SUPER_INFINITY_HEAD = REGISTRY.register("super_infinity_head", SuperInfinityArmorItem.SuperInfinityHead::new);
	public static final RegistryObject<Item> SUPER_INFINITY_CHEST = REGISTRY.register("super_infinity_chest", SuperInfinityArmorItem.SuperInfinityChest::new);
	public static final RegistryObject<Item> SUPER_INFINITY_LEG = REGISTRY.register("super_infinity_leg", SuperInfinityArmorItem.SuperInfinityLeg::new);
	public static final RegistryObject<Item> SUPER_INFINITY_BOOTS = REGISTRY.register("super_infinity_boots", SuperInfinityArmorItem.SuperInfinityBoots::new);
	public static final RegistryObject<Item> NEUTRON_BOW = REGISTRY.register("neutron_bow", NeutronBowItem::new);
	public static final RegistryObject<Item> INFINITY_AXE = REGISTRY.register("infinity_axe", InfinityAxeItem::new);
	public static final RegistryObject<Item> INFINITY_PICKAXE = REGISTRY.register("infinity_pickaxe", InfinityPickaxeItem::new);
	public static final RegistryObject<Item> INFINITY_SHOVEL = REGISTRY.register("infinity_shovel", InfinityShovelItem::new);
	public static final RegistryObject<Item> INFINITY_HOE = REGISTRY.register("infinity_hoe", InfinityHoeItem::new);
	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
