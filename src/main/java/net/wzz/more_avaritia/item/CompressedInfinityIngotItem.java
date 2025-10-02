
package net.wzz.more_avaritia.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class CompressedInfinityIngotItem extends Item {
	public CompressedInfinityIngotItem() {
		super(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.EPIC));
	}
}
