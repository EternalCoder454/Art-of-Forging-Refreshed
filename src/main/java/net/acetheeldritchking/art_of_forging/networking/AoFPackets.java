package net.acetheeldritchking.art_of_forging.networking;

import net.acetheeldritchking.art_of_forging.networking.packet.LifeStealPacketHandler;
import net.acetheeldritchking.art_of_forging.networking.packet.LifeStealParticlesS2CPacket;
import net.acetheeldritchking.art_of_forging.networking.packet.SoulChargedPacketHandler;
import net.acetheeldritchking.art_of_forging.networking.packet.SoulChargedParticlesS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * The two clientbound particle packets.
 *
 * <p>SimpleChannel and its message builder are gone. Payloads are registered against a registrar
 * on a mod bus event, and the handler is given where the payload is registered rather than living
 * on the packet. Both of these only tell a client where to draw particles.
 */
public class AoFPackets {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToClient(LifeStealParticlesS2CPacket.TYPE,
                LifeStealParticlesS2CPacket.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> LifeStealPacketHandler
                        .doLifestealParticles(payload.xPos(), payload.yPos(), payload.zPos())));

        registrar.playToClient(SoulChargedParticlesS2CPacket.TYPE,
                SoulChargedParticlesS2CPacket.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> SoulChargedPacketHandler
                        .doSoulParticles(payload.xPos(), payload.yPos(), payload.zPos())));
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static void sendToEntity(CustomPacketPayload message, LivingEntity entity) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, message);
    }
}
