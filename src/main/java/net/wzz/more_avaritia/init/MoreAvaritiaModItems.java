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

public class MoreAvaritiaModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MoreAvaritiaMod.MODID);
	public static final RegistryObject<Item> INFINITY_SWORD = REGISTRY.register("infinity_sword", () -> new InfinitySwordItem());
	public static final RegistryObject<Item> INFINITY_PICKAXE = REGISTRY.register("infinity_pickaxe", () -> new InfinityPickaxeItem());

	public static final RegistryObject<Item> INFINITY_AXE = REGISTRY.register("infinity_axe", () -> new InfinityAxeItem());
	public static final RegistryObject<Item> INFINITY_SHOVEL = REGISTRY.register("infinity_shovel", () -> new InfinityShovelItem());
	public static final RegistryObject<Item> INFINITY_HOE = REGISTRY.register("infinity_hoe", () -> new InfinityHoeItem());
	public static final RegistryObject<Item> INFINITY_BOW = REGISTRY.register("infinity_bow", () -> new InfinityBowItem());
	public static final RegistryObject<Item> INFINITY_APPLE = REGISTRY.register("infinity_apple", () -> new InfinityAppleItem());
	public static final RegistryObject<Item> NEUTRON_HELMET = REGISTRY.register("neutron_helmet", () -> new NeutronItem.Helmet());
	public static final RegistryObject<Item> NEUTRON_CHESTPLATE = REGISTRY.register("neutron_chestplate", () -> new NeutronItem.Chestplate());
	public static final RegistryObject<Item> NEUTRON_LEGGINGS = REGISTRY.register("neutron_leggings", () -> new NeutronItem.Leggings());
	public static final RegistryObject<Item> NEUTRON_BOOTS = REGISTRY.register("neutron_boots", () -> new NeutronItem.Boots());
	public static final RegistryObject<Item> INFINITY_FAKE_HELMET = REGISTRY.register("infinity_fake_helmet", () -> new InfinityFakeItem.Helmet());
	public static final RegistryObject<Item> INFINITY_FAKE_CHESTPLATE = REGISTRY.register("infinity_fake_chestplate", () -> new InfinityFakeItem.Chestplate());
	public static final RegistryObject<Item> INFINITY_FAKE_LEGGINGS = REGISTRY.register("infinity_fake_leggings", () -> new InfinityFakeItem.Leggings());
	public static final RegistryObject<Item> INFINITY_FAKE_BOOTS = REGISTRY.register("infinity_fake_boots", () -> new InfinityFakeItem.Boots());
	public static final RegistryObject<Item> NEUTRON_BOW = REGISTRY.register("neutron_bow", () -> new NeutronBowItem());
	public static final RegistryObject<Item> COMPRESSED_INFINITY = block(MoreAvaritiaModBlocks.COMPRESSED_INFINITY);
	public static final RegistryObject<Item> COMPRESSED_INFINITY_INGOT = REGISTRY.register("compressed_infinity_ingot", () -> new CompressedInfinityIngotItem());
	public static final RegistryObject<Item> INFINITY_GOD_SWORD = REGISTRY.register("infinity_god_sword", InfinityGodSwordItem::new);
	public static final RegistryObject<Item> UNIVERSE_HEART = REGISTRY.register("universe_heart", () -> new UniverseHeartItem());
	public static final RegistryObject<Item> ROD_RULING = REGISTRY.register("rod_ruling", () -> new RodRulingItem());
	public static final RegistryObject<Item> RESOUND_SWORD = REGISTRY.register("resound_sword", () -> new ResoundSwordItem());
	public static final RegistryObject<Item> INFINITY_HEAD = REGISTRY.register("infinity_head", () -> new InfinityArmorItem.Helmet());
	public static final RegistryObject<Item> INFINITY_Chestplate = REGISTRY.register("infinity_chestplate", () -> new InfinityArmorItem.Chestplate());
	public static final RegistryObject<Item> INFINITY_LEGS = REGISTRY.register("infinity_legs", () -> new InfinityArmorItem.Leggings());
	public static final RegistryObject<Item> INFINITY_BOOTS = REGISTRY.register("infinity_boots", () -> new InfinityArmorItem.Boots());

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
