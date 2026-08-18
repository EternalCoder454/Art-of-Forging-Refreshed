package net.acetheeldritchking.art_of_forging.util;

import net.acetheeldritchking.art_of_forging.ArtOfForging;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.Registries;

public class AoFTags {
    // Don't ever ask me to do entity tags again /hj
    public static class Entities {
        public static final TagKey<EntityType<?>> BOSS_ENTITIES = TagKey.create
                (Registries.ENTITY_TYPE,
                        Identifier.fromNamespaceAndPath(ArtOfForging.MOD_ID, "bosses"));
    }
}
