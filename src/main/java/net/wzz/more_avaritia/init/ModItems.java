package net.wzz.more_avaritia.init;

import net.wzz.more_avaritia.item.*;
import net.wzz.more_avaritia.MoreAvaritiaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.wzz.more_avaritia.item.bow.InfinityBowItem;
import net.wzz.more_avaritia.item.bow.NeutronBowItem;
import net.wzz.more_avaritia.item.food.InfinityAppleItem;
import net.wzz.more_avaritia.item.tools.*;

public class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MoreAvaritiaMod.MODID);
	public static final RegistryObject<Item> INFINITY_SWORD = REGISTRY.register("infinity_sword", InfinitySwordItem::new);
	public static final RegistryObject<Item> INFINITY_PICKAXE = REGISTRY.register("infinity_pickaxe", InfinityPickaxeItem::new);
	public static final RegistryObject<Item> INFINITY_AXE = REGISTRY.register("infinity_axe", InfinityAxeItem::new);
	public static final RegistryObject<Item> INFINITY_SHOVEL = REGISTRY.register("infinity_shovel", InfinityShovelItem::new);
	public static final RegistryObject<Item> INFINITY_HOE = REGISTRY.register("infinity_hoe", InfinityHoeItem::new);
	public static final RegistryObject<Item> INFINITY_BOW = REGISTRY.register("infinity_bow", InfinityBowItem::new);
	public static final RegistryObject<Item> INFINITY_APPLE = REGISTRY.register("infinity_apple", InfinityAppleItem::new);
	public static final RegistryObject<Item> NEUTRON_HELMET = REGISTRY.register("neutron_helmet", NeutronItem.Helmet::new);
	public static final RegistryObject<Item> NEUTRON_CHESTPLATE = REGISTRY.register("neutron_chestplate", NeutronItem.Chestplate::new);
	public static final RegistryObject<Item> NEUTRON_LEGGINGS = REGISTRY.register("neutron_leggings", NeutronItem.Leggings::new);
	public static final RegistryObject<Item> NEUTRON_BOOTS = REGISTRY.register("neutron_boots", NeutronItem.Boots::new);
	public static final RegistryObject<Item> INFINITY_FAKE_HELMET = REGISTRY.register("infinity_fake_helmet", InfinityFakeItem.Helmet::new);
	public static final RegistryObject<Item> INFINITY_FAKE_CHESTPLATE = REGISTRY.register("infinity_fake_chestplate", InfinityFakeItem.Chestplate::new);
	public static final RegistryObject<Item> INFINITY_FAKE_LEGGINGS = REGISTRY.register("infinity_fake_leggings", InfinityFakeItem.Leggings::new);
	public static final RegistryObject<Item> INFINITY_FAKE_BOOTS = REGISTRY.register("infinity_fake_boots", InfinityFakeItem.Boots::new);
	public static final RegistryObject<Item> NEUTRON_BOW = REGISTRY.register("neutron_bow", NeutronBowItem::new);
	public static final RegistryObject<Item> COMPRESSED_INFINITY = block(ModBlocks.COMPRESSED_INFINITY);
	public static final RegistryObject<Item> COMPRESSED_INFINITY_INGOT = REGISTRY.register("compressed_infinity_ingot", CompressedInfinityIngotItem::new);
	public static final RegistryObject<Item> INFINITY_GOD_SWORD = REGISTRY.register("infinity_god_sword", InfinityGodSwordItem::new);
	public static final RegistryObject<Item> UNIVERSE_HEART = REGISTRY.register("universe_heart", UniverseHeartItem::new);
	public static final RegistryObject<Item> ROD_RULING = REGISTRY.register("rod_ruling", RodRulingItem::new);
	public static final RegistryObject<Item> RESOUND_SWORD = REGISTRY.register("resound_sword", ResoundSwordItem::new);
	public static final RegistryObject<Item> INFINITY_HEAD = REGISTRY.register("infinity_head", InfinityArmorItem.Helmet::new);
	public static final RegistryObject<Item> INFINITY_Chestplate = REGISTRY.register("infinity_chestplate", InfinityArmorItem.Chestplate::new);
	public static final RegistryObject<Item> INFINITY_LEGS = REGISTRY.register("infinity_legs", InfinityArmorItem.Leggings::new);
	public static final RegistryObject<Item> INFINITY_BOOTS = REGISTRY.register("infinity_boots", InfinityArmorItem.Boots::new);
	public static final RegistryObject<Item> LIGHTING_STAFF = REGISTRY.register("lighting_staff", LightningStaffItem::new);

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
