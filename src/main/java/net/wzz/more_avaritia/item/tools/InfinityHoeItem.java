
package net.wzz.more_avaritia.item.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityHoeItem extends committee.nova.mods.avaritia.common.item.tools.infinity.InfinityHoeItem {
	public InfinityHoeItem() {
		super();
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("item.infinity_hoe.text.1"));
	}
}
