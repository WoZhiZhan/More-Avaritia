
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
public class ItemNeutronArmor extends ElementsMoreAvaritiaMod.ModElement {
	@GameRegistry.ObjectHolder("more_avaritia:neutron_armorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("more_avaritia:neutron_armorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("more_avaritia:neutron_armorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("more_avaritia:neutron_armorboots")
	public static final Item boots = null;
	public ItemNeutronArmor(ElementsMoreAvaritiaMod instance) {
		super(instance, 11);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("NEUTRON_ARMOR", "more_avaritia:neutron_", 500, new int[]{8, 12, 15, 10}, 90,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("")), 2f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("neutron_armorhelmet")
				.setRegistryName("neutron_armorhelmet").setCreativeTab(Avaritia.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("neutron_armorbody")
				.setRegistryName("neutron_armorbody").setCreativeTab(Avaritia.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("neutron_armorlegs")
				.setRegistryName("neutron_armorlegs").setCreativeTab(Avaritia.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("neutron_armorboots")
				.setRegistryName("neutron_armorboots").setCreativeTab(Avaritia.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("more_avaritia:neutron_armorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("more_avaritia:neutron_armorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("more_avaritia:neutron_armorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("more_avaritia:neutron_armorboots", "inventory"));
	}
}
