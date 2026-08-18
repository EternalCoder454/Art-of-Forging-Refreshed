package net.acetheeldritchking.art_of_forging.capabilities;

import net.acetheeldritchking.art_of_forging.ArtOfForging;
import net.acetheeldritchking.art_of_forging.capabilities.carnage.PlayerCarnage;
import net.acetheeldritchking.art_of_forging.capabilities.conquer.PlayerConquer;
import net.acetheeldritchking.art_of_forging.capabilities.devouring.PlayerDevouring;
import net.acetheeldritchking.art_of_forging.capabilities.karma.PlayerKarma;
import net.acetheeldritchking.art_of_forging.capabilities.soulCharge.PlayerSoulCharge;
import net.acetheeldritchking.art_of_forging.capabilities.subjugation.PlayerSubjugation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * The six per player values this mod keeps, as data attachments.
 *
 * <p>These were Forge capabilities, which needed a provider, a LazyOptional and an attach event
 * each. An attachment is registered like anything else and read straight off the entity, so all of
 * that went away. Every one of them survives death, which is what the old copy on clone handler in
 * AoFEvents was doing by hand.
 */
public class AoFAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ArtOfForging.MOD_ID);

    public static final Supplier<AttachmentType<PlayerCarnage>> CARNAGE =
            ATTACHMENT_TYPES.register("carnage", () -> AttachmentType
                    .serializable(PlayerCarnage::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<PlayerConquer>> CONQUER =
            ATTACHMENT_TYPES.register("conquer", () -> AttachmentType
                    .serializable(PlayerConquer::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<PlayerDevouring>> DEVOURING =
            ATTACHMENT_TYPES.register("devouring", () -> AttachmentType
                    .serializable(PlayerDevouring::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<PlayerKarma>> KARMA =
            ATTACHMENT_TYPES.register("karma", () -> AttachmentType
                    .serializable(PlayerKarma::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<PlayerSoulCharge>> SOUL_CHARGE =
            ATTACHMENT_TYPES.register("soul_charge", () -> AttachmentType
                    .serializable(PlayerSoulCharge::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<PlayerSubjugation>> SUBJUGATION =
            ATTACHMENT_TYPES.register("subjugation", () -> AttachmentType
                    .serializable(PlayerSubjugation::new).copyOnDeath().build());

}
