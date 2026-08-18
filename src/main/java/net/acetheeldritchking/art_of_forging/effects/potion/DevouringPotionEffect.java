package net.acetheeldritchking.art_of_forging.effects.potion;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DevouringPotionEffect extends MobEffect {
    public DevouringPotionEffect() {
        super(MobEffectCategory.HARMFUL, 13041721);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.hurt(pLivingEntity.damageSources().magic(), pAmplifier);

        if (!pLivingEntity.level().isClientSide()) {
            ServerLevel world = (ServerLevel) pLivingEntity.level();

            world.sendParticles(new DustColorTransitionOptions(0x802e2e, 0xf21a1a, 1.5F),
                    pLivingEntity.getX(), pLivingEntity.getY(0.5), pLivingEntity.getZ(), 25, 0.5D, 0.3D, 0.5D, 0.0D);
        }
    
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }
}
