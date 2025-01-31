package com.wzz.more_avaritia;

import com.wzz.more_avaritia.ItemShander.CosmicItemRenderer;
import com.wzz.more_avaritia.ItemShander.ShaderHelper;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;


public class ClientProxy extends CommonProxy {
    @Override
    public void PreInitialization(FMLPreInitializationEvent e) {
        super.PreInitialization(e);
    }

    @Override
    public void Initialization(FMLInitializationEvent e) {
        super.Initialization(e);
        CosmicItemRenderer c = new CosmicItemRenderer();
        MinecraftForgeClient.registerItemRenderer((Item)ItemInfinitySingularity.itemInfinitySingularity, (IItemRenderer)c);
        MinecraftForgeClient.registerItemRenderer((Item)ItemInfinitySword.itemInfinitySword, (IItemRenderer)c);
        MinecraftForgeClient.registerItemRenderer((Item)ItemInfinityBow.itemInfinityBow, (IItemRenderer)c);
        ShaderHelper.initShaders();
    }

    @Override
    public void PostInitialization(FMLPostInitializationEvent e) {
        super.PostInitialization(e);
    }
}
