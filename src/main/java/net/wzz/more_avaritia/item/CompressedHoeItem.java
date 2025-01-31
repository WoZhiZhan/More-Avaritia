
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;

public class CompressedHoeItem extends HoeItem {
	public CompressedHoeItem() {
		super(new Tier() {
			public int getUses() {
				return 16000;
			}

			public float getSpeed() {
				return 130f;
			}

			public float getAttackDamageBonus() {
				return 200f;
			}

			public int getLevel() {
				return 130;
			}

			public int getEnchantmentValue() {
				return 100;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(MoreAvaritiaModItems.COMPRESSED_NEUTRON_INGOT.get()));
			}
		}, 0, -2.8f, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
	}
}
