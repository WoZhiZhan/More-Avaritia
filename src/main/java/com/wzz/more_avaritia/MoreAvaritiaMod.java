package com.wzz.more_avaritia;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(name = "more_avaritia",modid = "more_avaritia")
public class MoreAvaritiaMod {
    @SidedProxy(clientSide = "com.wzz.more_avaritia.ClientProxy", serverSide = "com.wzz.more_avaritia.CommonProxy")
    public static CommonProxy Proxy;

    @Mod.EventHandler
    public void OnPreInitialization(FMLPreInitializationEvent e) {
        Proxy.PreInitialization(e);
    }

    @Mod.EventHandler
    public void OnInitialization(FMLInitializationEvent e) {
        Proxy.Initialization(e);
    }

    @Mod.EventHandler
    public void OnPostInitlization(FMLPostInitializationEvent e) {
        Proxy.PostInitialization(e);
    }
}
