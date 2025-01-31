package com.wzz.more_avaritia;

import com.wzz.more_avaritia.ItemShander.ICosmicRenderItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.DamageSourceInfinitySword;
import fox.spiteful.avaritia.items.tools.ItemSwordInfinity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;

import java.util.List;

public class ItemInfinitySword extends ItemSwordInfinity implements ICosmicRenderItem {
    private IIcon cosmicMask;
    private IIcon pommel;
    public static ItemInfinitySword itemInfinitySword;
    public ItemInfinitySword() {
        super();
        setUnlocalizedName("infinity_sword");
        setCreativeTab(Avaritia.tab);
        setTextureName("more_avaritia:infinity_sword");
        itemInfinitySword = this;
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
        this.cosmicMask = ir.registerIcon("more_avaritia:infinity_sword");
        this.pommel = ir.registerIcon("more_avaritia:infinity_sword");
    }
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1)
            return this.pommel;
        return super.getIcon(stack, pass);
    }
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add("new:\u5de6\u952e\u5c0f\u8303\u56f4\u653b\u51fb\uff0c\u65e0\u89c6\u8ddd\u79bb");
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        Vec3 Vector = entityLiving.getLookVec();
        for (int i = 0; i < 512; i++) {
            double x = Vector.xCoord * i;
            double y = entityLiving.getEyeHeight() + Vector.yCoord * i;
            double z = Vector.zCoord * i;
            List<Entity> Entities = entityLiving.worldObj.getEntitiesWithinAABBExcludingEntity(Minecraft.getMinecraft().thePlayer,
                    entityLiving.boundingBox.expand(1D, 1D, 1D).offset(x, y, z));
            for (Entity e : Entities) {
                if (!(e instanceof EntityPlayer)) {
                    if (e instanceof EntityLivingBase) {
                        EntityLivingBase livingBase = (EntityLivingBase) e;
                        livingBase.func_110142_aN().func_94547_a(new DamageSourceInfinitySword(entityLiving), livingBase.getHealth(), livingBase.getHealth());
                        livingBase.setHealth(0.0F);
                        livingBase.onDeath(new EntityDamageSource("infinity", entityLiving));
                    }
                }
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return super.getItemStackDisplayName(p_77653_1_)+"\u00a7\u0065\u00b7\u6539";
    }
}
