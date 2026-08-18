package net.acetheeldritchking.art_of_forging.capabilities.subjugation;

import net.acetheeldritchking.art_of_forging.capabilities.AoFAttachments;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

/**
 * Reaches the subjugation value hung off an entity.
 *
 * <p>This was a capability provider. Capabilities are gone, so it is an attachment now, and all
 * that is left here is the lookup. It still hands back an Optional so the callers that were written
 * against the capability api keep working unchanged.
 */
public class PlayerSubjugationProvider {
    private PlayerSubjugationProvider() {
    }

    /**
     * {@return the subjugation value for this entity, empty for anything that is not a player}
     */
    public static Optional<PlayerSubjugation> get(Entity entity) {
        if (!(entity instanceof net.minecraft.world.entity.player.Player)) {
            return Optional.empty();
        }
        return Optional.of(entity.getData(AoFAttachments.SUBJUGATION));
    }
}
