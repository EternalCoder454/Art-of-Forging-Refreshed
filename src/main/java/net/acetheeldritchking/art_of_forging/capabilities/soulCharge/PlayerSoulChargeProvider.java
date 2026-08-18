package net.acetheeldritchking.art_of_forging.capabilities.soulCharge;

import net.acetheeldritchking.art_of_forging.capabilities.AoFAttachments;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

/**
 * Reaches the soul_charge value hung off an entity.
 *
 * <p>This was a capability provider. Capabilities are gone, so it is an attachment now, and all
 * that is left here is the lookup. It still hands back an Optional so the callers that were written
 * against the capability api keep working unchanged.
 */
public class PlayerSoulChargeProvider {
    private PlayerSoulChargeProvider() {
    }

    /**
     * {@return the soul_charge value for this entity, empty for anything that is not a player}
     */
    public static Optional<PlayerSoulCharge> get(Entity entity) {
        if (!(entity instanceof net.minecraft.world.entity.player.Player)) {
            return Optional.empty();
        }
        return Optional.of(entity.getData(AoFAttachments.SOUL_CHARGE));
    }
}
