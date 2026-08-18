package net.acetheeldritchking.art_of_forging.networking.packet;

import net.acetheeldritchking.art_of_forging.ArtOfForging;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Where to draw the ring of particles a soul charged hit leaves.
 *
 * <p>This was a class with a byte buffer constructor and a handle method. A packet is a payload
 * now, so it carries its own type and stream codec and the handling lives where it is registered.
 */
public record SoulChargedParticlesS2CPacket(double xPos, double yPos, double zPos) implements CustomPacketPayload {
    public static final Type<SoulChargedParticlesS2CPacket> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(ArtOfForging.MOD_ID, "soul_charged_particles"));

    public static final StreamCodec<FriendlyByteBuf, SoulChargedParticlesS2CPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> {
                buffer.writeDouble(packet.xPos());
                buffer.writeDouble(packet.yPos());
                buffer.writeDouble(packet.zPos());
            },
            buffer -> new SoulChargedParticlesS2CPacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
