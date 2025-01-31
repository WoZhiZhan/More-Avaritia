
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class CoalSingularityItem extends Item {
	public CoalSingularityItem() {
		super(new Item.Properties().tab(AvaritiaModContent.TAB).stacksTo(64).fireResistant().rarity(Rarity.RARE));
	}
}
