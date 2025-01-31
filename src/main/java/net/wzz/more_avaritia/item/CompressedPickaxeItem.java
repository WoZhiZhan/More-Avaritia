
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class CompressedPickaxeItem extends PickaxeItem {
	public CompressedPickaxeItem() {
		super(new Tier() {
			public int getUses() {
				return 16000;
			}

			public float getSpeed() {
				return 100f;
			}

			public float getAttackDamageBonus() {
				return 300f;
			}

			public int getLevel() {
				return 100;
			}

			public int getEnchantmentValue() {
				return 100;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(MoreAvaritiaModItems.COMPRESSED_NEUTRON_INGOT.get()));
			}
		}, 1, -2.8f, new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant());
	}
}
