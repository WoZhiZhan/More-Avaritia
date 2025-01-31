package com.wzz.more_avaritia;

import com.wzz.more_avaritia.ItemShander.ICosmicRenderItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemInfinitySingularity extends Item implements ICosmicRenderItem {
    private IIcon cosmicMask;
    private IIcon pommel;
    public static ItemInfinitySingularity itemInfinitySingularity;
    public ItemInfinitySingularity() {
        super();
        setUnlocalizedName("infinity_singularity");
        setTextureName("more_avaritia:infinity_singularity");
        setCreativeTab(Avaritia.tab);
        itemInfinitySingularity = this;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
        return false;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getMaskTexture(ItemStack stack, EntityPlayer player) {
        return this.cosmicMask;
    }

    @SideOnly(Side.CLIENT)
    public float getMaskMultiplier(ItemStack stack, EntityPlayer player) {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        super.registerIcons(ir);
        this.cosmicMask = ir.registerIcon("more_avaritia:layer_0");
        this.pommel = ir.registerIcon("more_avaritia:infinity_singularity");
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return RainbowText.Rainbow("\u5947\u70b9-\u65e0\u5c3d");
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1)
            return this.pommel;
        return super.getIcon(stack, pass);
    }
}
