
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wzz.more_avaritia.init;

import net.wzz.more_avaritia.block.CompressedNeutronBlockBlock;
import net.wzz.more_avaritia.block.CompressedInfinityBlockBlock;
import net.wzz.more_avaritia.block.ChargingCrystalOreBlock;
import net.wzz.more_avaritia.MoreAvaritiaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class MoreAvaritiaModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreAvaritiaMod.MODID);
	public static final RegistryObject<Block> COMPRESSED_NEUTRON_BLOCK = REGISTRY.register("compressed_neutron_block", () -> new CompressedNeutronBlockBlock());
	public static final RegistryObject<Block> COMPRESSED_INFINITY_BLOCK = REGISTRY.register("compressed_infinity_block", () -> new CompressedInfinityBlockBlock());
	public static final RegistryObject<Block> CHARGING_CRYSTAL_ORE = REGISTRY.register("charging_crystal_ore", () -> new ChargingCrystalOreBlock());
}
