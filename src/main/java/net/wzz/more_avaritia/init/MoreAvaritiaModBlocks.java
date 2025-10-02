
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wzz.more_avaritia.init;

import net.wzz.more_avaritia.block.CompressedInfinityBlock;
import net.wzz.more_avaritia.MoreAvaritiaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class MoreAvaritiaModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreAvaritiaMod.MODID);
	public static final RegistryObject<Block> COMPRESSED_INFINITY = REGISTRY.register("compressed_infinity", () -> new CompressedInfinityBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
