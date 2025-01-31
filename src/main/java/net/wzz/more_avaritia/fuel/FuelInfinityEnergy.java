
package net.wzz.more_avaritia.fuel;

import net.wzz.more_avaritia.item.ItemEndlessEnergy;
import net.wzz.more_avaritia.ElementsMoreAvaritiaMod;

import net.minecraft.item.ItemStack;

@ElementsMoreAvaritiaMod.ModElement.Tag
public class FuelInfinityEnergy extends ElementsMoreAvaritiaMod.ModElement {
	public FuelInfinityEnergy(ElementsMoreAvaritiaMod instance) {
		super(instance, 4);
	}

	@Override
	public int addFuel(ItemStack fuel) {
		if (fuel.getItem() == new ItemStack(ItemEndlessEnergy.block, (int) (1)).getItem())
			return 1000000000;
		return 0;
	}
}
