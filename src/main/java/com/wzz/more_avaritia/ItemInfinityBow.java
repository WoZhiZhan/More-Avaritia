package com.wzz.more_avaritia;

import com.wzz.more_avaritia.ItemShander.ICosmicRenderItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.tools.ItemBowInfinity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemInfinityBow extends ItemBowInfinity implements ICosmicRenderItem {
    private IIcon cosmicMask;
    private IIcon pommel;
    public static ItemInfinityBow itemInfinityBow;
    public ItemInfinityBow() {
        super();
        setUnlocalizedName("infinity_bow");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:infinity_bow_standby");
        itemInfinityBow = this;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getMaskTexture(ItemStack stack, EntityPlayer player) {
        return this.cosmicMask;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return super.getItemStackDisplayName(p_77653_1_)+"\u00a7\u0065\u00b7\u6539";
    }

    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        this.fire(stack, player.worldObj, player, 0);
    }

    @SideOnly(Side.CLIENT)
    public float getMaskMultiplier(ItemStack stack, EntityPlayer player) {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        super.registerIcons(ir);
        this.cosmicMask = ir.registerIcon("more_avaritia:infinity_bow_standby");
        this.pommel = ir.registerIcon("more_avaritia:infinity_bow_standby");
    }
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1)
            return this.pommel;
        return super.getIcon(stack, pass);
    }
}
