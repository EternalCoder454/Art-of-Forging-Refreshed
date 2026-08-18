package net.acetheeldritchking.art_of_forging.effects.potion;

import net.acetheeldritchking.art_of_forging.ArtOfForging;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PotionEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT,
                    ArtOfForging.MOD_ID);

    // Evoking Maw Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> EVOKING_MAW =
            MOB_EFFECTS.register("evoking_maw", EvokingMawPotionEffect::new);

    // Defuse Creeper Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> DEFUSE_CREEPER =
            MOB_EFFECTS.register("defuse_creeper", DefuseCreeperPotionEffect::new);

    // Devouring Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> DEVOURING =
            MOB_EFFECTS.register("devouring", DevouringPotionEffect::new);

    // Mortal Wounds Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> MORTAL_WOUNDS =
            MOB_EFFECTS.register("mortal_wounds", MortalWoundsPotionEffect::new);

    // Targeted Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> TARGETED =
            MOB_EFFECTS.register("targeted", TargetedPotionEffect::new);

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }
}
