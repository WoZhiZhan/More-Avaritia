
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.AxeItem;

public class CompressedAxeItem extends AxeItem {
	public CompressedAxeItem() {
		super(new Tier() {
			public int getUses() {
				return 16000;
			}

			public float getSpeed() {
				return 130f;
			}

			public float getAttackDamageBonus() {
				return 1600f;
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
		}, 1, -3.1f, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
	}
}
