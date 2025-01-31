package net.wzz.more_avaritia.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.player.Player;
import net.wzz.more_avaritia.event.MoreClientInit;
import net.wzz.more_avaritia.event.EventHandle;

public class PlayerRender extends RenderLayer<Player, PlayerModel<Player>> {
    public Iterable<ModelPart> b() {
      return (Iterable<ModelPart>) ImmutableList.of(((PlayerModel)getParentModel()).head, ((PlayerModel)getParentModel()).hat, ((PlayerModel)getParentModel()).body, ((PlayerModel)getParentModel()).leftArm, ((PlayerModel)getParentModel()).rightArm, ((PlayerModel)getParentModel()).leftLeg, ((PlayerModel)getParentModel()).rightLeg);
    }
    
    public PlayerRender(RenderLayerParent<Player, PlayerModel<Player>> t) {
      super(t);
    }
    
    public void render(PoseStack m, MultiBufferSource b, int i1, Player l, float ff1, float ff2, float ff3, float ff4, float ff5, float ff6) {
      if (EventHandle.hasInfinityArmor(l)) {
        MoreClientInit.cosmicOpacity2.set(0.8F);
        b().forEach(t -> t.render(m, InfinityArmorModel.mat(MoreClientInit.MASK_INV).buffer(b, InfinityArmorModel::mask2), i1, 1, 1.0F, 1.0F, 1.0F, 1.0F));
      } 
    }
  }