
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CompressedInfinityIngotItem extends Item {
	public CompressedInfinityIngotItem() {
		super(new Item.Properties().tab(AvaritiaModContent.TAB).stacksTo(64).fireResistant().rarity(Rarity.EPIC));
	}
}
