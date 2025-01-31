
package net.wzz.more_avaritia.creativetab;

import morph.avaritia.init.ModItems;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.creativetab.CreativeTabs;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class TabBigOre extends ElementsMoreAvaritiaMod.ModElement {
	public TabBigOre(ElementsMoreAvaritiaMod instance) {
		super(instance, 69);
	}

	@Override
	public void initElements() {
		tab = new CreativeTabs("tabbig_ore") {
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ModItems.infinity_pickaxe, 1);
			}

			@SideOnly(Side.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
	public static CreativeTabs tab;
}
