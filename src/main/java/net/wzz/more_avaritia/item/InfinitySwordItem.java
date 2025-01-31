
package net.wzz.more_avaritia.item;

import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.wzz.more_avaritia.util.ListHelper;
import net.wzz.more_avaritia.util.RainbowText;

import java.util.List;

import static morph.avaritia.init.AvaritiaModContent.COSMIC_RARITY;

public class InfinitySwordItem extends morph.avaritia.item.tools.InfinitySwordItem {
	public InfinitySwordItem() {
		super(new Item.Properties().tab(AvaritiaModContent.TAB).fireResistant().rarity(COSMIC_RARITY));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity living) {
			if (!(living instanceof Player))
				ListHelper.addEntityToList(living);
			living.setSecondsOnFire(1000);
			living.hurt(DamageSource.mobAttack(player), Float.POSITIVE_INFINITY);
			living.setLastHurtByPlayer(player);
			living.setHealth(0);
			if (!(living instanceof Player))
				living.die(DamageSource.OUT_OF_WORLD);
			if (living.getHealth() > 0) {
				living.setInvisible(true);
				living.discard();
				player.displayClientMessage(new TranslatableComponent("nodie.message"), true);
			}
		}
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TranslatableComponent("item.infinity_sword.text1"));
		list.add(new TextComponent(RainbowText.makeColour("上乘寰宇之志下震诸侯万民")));
	}
}
