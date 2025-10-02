package net.wzz.more_avaritia.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.wzz.more_avaritia.MoreAvaritiaMod;
import net.wzz.more_avaritia.util.InfinityUtils;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEntityRemovePacket {
    public final int uuid;

    public ClientEntityRemovePacket(int uuid) {
        this.uuid = uuid;
    }

    public ClientEntityRemovePacket(FriendlyByteBuf buffer) {
        this.uuid = buffer.readInt();
    }

    public static void encode(ClientEntityRemovePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.uuid);
    }

    public static void handle(ClientEntityRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->  {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                handleClient(msg, ctx);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ClientEntityRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (Minecraft.getInstance().level != null) {
            Entity entity = Minecraft.getInstance().level.getEntity(msg.uuid);
            if (entity instanceof LivingEntity living)
                InfinityUtils.killEntity(living, null);
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MoreAvaritiaMod.addNetworkMessage(ClientEntityRemovePacket.class,
                ClientEntityRemovePacket::encode,
                ClientEntityRemovePacket::new,
                ClientEntityRemovePacket::handle);
    }
}