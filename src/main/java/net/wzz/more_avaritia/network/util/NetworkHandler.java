package net.wzz.more_avaritia.network.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.wzz.more_avaritia.MoreAvaritiaMod;

import java.util.List;
import java.util.function.Supplier;

public class NetworkHandler {
    /**
     * 向指定位置附近的所有玩家发送数据包
     * @param packet 要发送的数据包
     * @param level 世界对象
     * @param position 中心位置
     * @param radius 半径范围
     */
    public static void sendToNearbyPlayers(Object packet, Level level, Vec3 position, double radius) {
        if (level instanceof ServerLevel serverLevel) {
            // 获取范围内所有玩家
            List<ServerPlayer> players = serverLevel.getPlayers(
                    player -> player.distanceToSqr(position.x, position.y, position.z) < radius * radius
            );
            for (ServerPlayer player : players) {
                MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), packet);
            }
        }
    }

    // 重载方法：使用实体作为中心点
    public static void sendToNearbyPlayers(Object packet, Entity entity, double radius) {
        sendToNearbyPlayers(packet, entity.level(), entity.position(), radius);
    }

    // ========== 从客户端向服务端发送 ==========

    /**
     * 从客户端向服务器发送数据包
     * @param packet 要发送的数据包
     */
    public static void sendToServer(Object packet) {
        MoreAvaritiaMod.PACKET_HANDLER.sendToServer(packet);
    }

    /**
     * 从客户端向服务器发送数据包（带安全检查）
     * @param packet 要发送的数据包
     */
    public static void sendToServerSafe(Object packet) {
        if (Minecraft.getInstance().getConnection() != null) {
            MoreAvaritiaMod.PACKET_HANDLER.sendToServer(packet);
        }
    }

    // ========== 从服务端向客户端发送 ==========

    /**
     * 向特定玩家发送数据包
     * @param packet 要发送的数据包
     * @param player 目标玩家
     */
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    /**
     * 向所有在线玩家发送数据包
     * @param packet 要发送的数据包
     */
    public static void sendToAllPlayers(Object packet) {
        MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void sendToAllPlayer(Level level, Object object) {
        if (!level.isClientSide()) {
            MinecraftServer mcServer = ServerLifecycleHooks.getCurrentServer();
            if (mcServer != null && !mcServer.getPlayerList().getPlayers().isEmpty())
                for (ServerPlayer serverPlayer : mcServer.getPlayerList().getPlayers())
                    send(PacketDistributor.PLAYER.with(() -> serverPlayer), object);
        }
    }

    public static void sendToAllPlayerWith(Level level, Object object, ServerPlayer source) {
        if (!level.isClientSide()) {
            MinecraftServer mcServer = ServerLifecycleHooks.getCurrentServer();
            if (mcServer != null && !mcServer.getPlayerList().getPlayers().isEmpty())
                for (ServerPlayer serverPlayer : mcServer.getPlayerList().getPlayers())
                    if (serverPlayer != source)
                        send(PacketDistributor.PLAYER.with(() -> serverPlayer), object);
        }
    }

    public static void send(PacketDistributor.PacketTarget target, Object object) {
        MoreAvaritiaMod.PACKET_HANDLER.send(target, object);
    }

    /**
     * 向所有跟踪特定实体的玩家发送数据包
     * @param packet 要发送的数据包
     * @param entity 目标实体
     */
    public static void sendToTrackingEntity(Object packet, Entity entity) {
        MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }

    /**
     * 向所有跟踪特定实体及其自身的玩家发送数据包
     * @param packet 要发送的数据包
     * @param entity 目标实体
     */
    public static void sendToTrackingEntityAndSelf(Object packet, Entity entity) {
        MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), packet);
    }

    /**
     * 向特定维度内的所有玩家发送数据包
     * @param packet 要发送的数据包
     * @param dimension 目标维度
     */
    public static void sendToDimension(Object packet, ResourceKey<Level> dimension) {
        MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(() -> dimension), packet);
    }

    /**
     * 向特定点附近的所有玩家发送数据包
     * @param packet 要发送的数据包
     * @param pos 目标位置
     * @param radius 半径
     */
    public static void sendToAllAround(Object packet, Vec3 pos, double radius, ResourceKey<Level> dimension) {
        MoreAvaritiaMod.PACKET_HANDLER.send(PacketDistributor.NEAR.with(() ->
                        new PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, radius, dimension)),
                packet);
    }

    // ========== 数据包处理工具方法 ==========

    /**
     * 获取发送数据包的玩家（服务端用）
     * @param ctx 网络上下文
     * @return 发送玩家，可能为null
     */
    public static ServerPlayer getSender(Supplier<NetworkEvent.Context> ctx) {
        return ctx.get().getSender();
    }

    /**
     * 安全处理数据包（自动检查方向）
     * @param ctx 网络上下文
     * @param serverHandler 服务端处理逻辑
     * @param clientHandler 客户端处理逻辑
     */
    public static void handlePacket(Supplier<NetworkEvent.Context> ctx,
                                    Runnable serverHandler,
                                    Runnable clientHandler) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                serverHandler.run();
            } else {
                clientHandler.run();
            }
        });
        context.setPacketHandled(true);
    }
}