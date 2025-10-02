
package net.wzz.more_avaritia.item.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityPickaxeItem extends committee.nova.mods.avaritia.common.item.tools.infinity.InfinityPickaxeItem {

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> list, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, list, p_41424_);
		list.add(Component.translatable("item.infinity_pickaxe.text.1"));
	}
}
