package net.acetheeldritchking.art_of_forging.effects.potion;

import net.acetheeldritchking.art_of_forging.ArtOfForging;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MortalWoundsPotionEffect extends MobEffect {
    protected MortalWoundsPotionEffect() {
        super(MobEffectCategory.HARMFUL, 11607582);
        this.addAttributeModifier(Attributes.MAX_HEALTH,
                Identifier.fromNamespaceAndPath(ArtOfForging.MOD_ID, "mortal_wounds"),
                -0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity pLivingEntity, int pAmplifier) {
        return super.applyEffectTick(level, pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 20 == 0;
    }
}
