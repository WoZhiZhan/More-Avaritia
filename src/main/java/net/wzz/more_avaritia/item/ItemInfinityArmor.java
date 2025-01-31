
package net.wzz.more_avaritia.item;

import morph.avaritia.Avaritia;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class ItemInfinityArmor extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:infinity_armorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("more_avaritia:infinity_armorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("more_avaritia:infinity_armorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("more_avaritia:infinity_armorboots")
	public static final Item boots = null;
	public ItemInfinityArmor(ElementsMoreAvaritiaMod instance) {
		super(instance, 22);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("INFINITY_ARMOR", "more_avaritia:infinity_", 25,
				new int[]{1024, 1024, 1024, 1024}, 100,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("")), 10f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("infinity_armorhelmet")
				.setRegistryName("infinity_armorhelmet").setCreativeTab(Avaritia.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("infinity_armorbody")
				.setRegistryName("infinity_armorbody").setCreativeTab(Avaritia.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("infinity_armorlegs")
				.setRegistryName("infinity_armorlegs").setCreativeTab(Avaritia.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("infinity_armorboots")
				.setRegistryName("infinity_armorboots").setCreativeTab(Avaritia.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("more_avaritia:infinity_armorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("more_avaritia:infinity_armorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("more_avaritia:infinity_armorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("more_avaritia:infinity_armorboots", "inventory"));
	}
}
