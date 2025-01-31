package com.wzz.more_avaritia;

import com.wzz.more_avaritia.ItemShander.event;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public static Item[] items = {
            new ItemInfinitySingularity(),
            new ItemCompressedNeutronIngot(),
            new ItemEndlessEnergy(),
            new ItemChargingCrystal(),
            new ItemNeutronSword(),
            new ItemNeutronPick(),
            new ItemNeutronAxe(),
            new ItemNeutronShovel(),
            new ItemNeutronHoe(),
            new ItemInfinitySword(),
            new ItemInfinityBow(),
            new ItemEndlessEnergy2(),
            new ItemEndlessEnergy3(),
            new ItemEndlessEnergy4(),
            new ItemEndlessEnergy5(),
            new ItemEndlessEnergy6(),
            new ItemEndlessEnergy7(),
            new ItemEndlessEnergy8(),
            new ItemEndlessEnergy9()
    };
    public static ItemArmor itemNeutronHelmet = new ItemNeutronArmor("neutron_helmet",0);
    public static ItemArmor itemNeutronChestplate = new ItemNeutronArmor("neutron_chestplate",1);
    public static ItemArmor itemNeutronLeggings = new ItemNeutronArmor("neutron_leggings",2);
    public static ItemArmor itemNeutronBoots = new ItemNeutronArmor("neutron_boots",3);
    public static void init() {
        for (Item item : items) {
            GameRegistry.registerItem(item, item.getUnlocalizedName());
        }
        GameRegistry.registerItem(itemNeutronHelmet,"neutron_helmet");
        GameRegistry.registerItem(itemNeutronChestplate,"neutron_chestplate");
        GameRegistry.registerItem(itemNeutronLeggings,"neutron_leggings");
        GameRegistry.registerItem(itemNeutronBoots,"neutron_boots");
        ItemStack inputItemA = new ItemStack(LudicrousItems.resource, 1, 6);
        ItemStack inputItemAA = new ItemStack(LudicrousItems.resource, 1, 4);
        ItemStack inputItemAAA = new ItemStack(LudicrousBlocks.resource_block, 1, 0);
        ItemStack inputItemAAAA = new ItemStack(LudicrousItems.infinity_sword);
        ItemStack inputItemAAAAA = new ItemStack(LudicrousItems.infinity_bow);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy.itemEndlessEnergy), "AAA", "ADA", "AAA", 'A', inputItemA, 'D', Items.coal);
        GameRegistry.addShapedRecipe(new ItemStack(ItemNeutronPick.itemNeutronPick), "AAA", " D ", " D ", 'A', inputItemAA, 'D', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemNeutronAxe.itemNeutronAxe), "AA ", "AD ", " D ", 'A', inputItemAA, 'D', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemNeutronShovel.itemNeutronShovel), " A ", " D ", " D ", 'A', inputItemAA, 'D', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemNeutronHoe.itemNeutronHoe), "AA ", " D ", " D ", 'A', inputItemAA, 'D', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemNeutronSword.itemNeutronSword), " A ", " A ", " D ", 'A', inputItemAA, 'D', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemCompressedNeutronIngot.itemCompressedNeutronIngot), "AAA", "AAA", "AAA", 'A', inputItemAAA);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy2.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy3.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy2.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy4.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy3.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy5.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy4.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy6.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy5.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy7.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy6.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy8.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy7.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemEndlessEnergy9.itemEndlessEnergy), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy8.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(ItemChargingCrystal.itemChargingCrystal), "AAA", "AAA", "AAA", 'A', ItemEndlessEnergy9.itemEndlessEnergy);
        GameRegistry.addShapedRecipe(new ItemStack(itemNeutronHelmet), "AAA", "A A", "   ", 'A', inputItemAA);
        GameRegistry.addShapedRecipe(new ItemStack(itemNeutronChestplate), "A A", "AAA", "AAA", 'A', inputItemAA);
        GameRegistry.addShapedRecipe(new ItemStack(itemNeutronLeggings), "AAA", "A A", "A A", 'A', inputItemAA);
        GameRegistry.addShapedRecipe(new ItemStack(itemNeutronBoots), "   ", "A A", "A A", 'A', inputItemAA);
        GameRegistry.addShapedRecipe(new ItemStack(ItemInfinitySword.itemInfinitySword), "AAC", "ACA", "BAA", 'A', ItemCompressedNeutronIngot.itemCompressedNeutronIngot, 'B',
                inputItemAAAA,'C',ItemChargingCrystal.itemChargingCrystal);
        GameRegistry.addShapedRecipe(new ItemStack(ItemInfinityBow.itemInfinityBow), "AAC", "ABC", "AAC", 'A', ItemCompressedNeutronIngot.itemCompressedNeutronIngot, 'B',
                inputItemAAAAA,'C',ItemChargingCrystal.itemChargingCrystal);
    }
    public void PreInitialization(FMLPreInitializationEvent event) {
        init();
    }

    public void Initialization(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new event());
        FMLCommonHandler.instance().bus().register(new event());
        MinecraftForge.EVENT_BUS.register(new FuelHandler());
        FMLCommonHandler.instance().bus().register(new FuelHandler());
    }

    public void PostInitialization(FMLPostInitializationEvent e) {

    }
}
