
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class ChargingCrystalItem extends Item {
	public ChargingCrystalItem() {
		super(new Item.Properties().tab(AvaritiaModContent.TAB).stacksTo(64).fireResistant().rarity(Rarity.EPIC));
	}
}
