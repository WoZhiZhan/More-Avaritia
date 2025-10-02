
package net.wzz.more_avaritia.item.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfinityAxeItem extends committee.nova.mods.avaritia.common.item.tools.infinity.InfinityAxeItem {
	public InfinityAxeItem() {
		super();
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
		super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
		p_41423_.add(Component.translatable("item.infinity_axe.text.1"));
	}
}
