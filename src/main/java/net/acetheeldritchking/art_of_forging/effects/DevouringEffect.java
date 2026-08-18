package net.acetheeldritchking.art_of_forging.effects;

import net.acetheeldritchking.art_of_forging.capabilities.devouring.PlayerDevouring;
import net.acetheeldritchking.art_of_forging.capabilities.devouring.PlayerDevouringProvider;
import net.acetheeldritchking.art_of_forging.effects.potion.PotionEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static net.acetheeldritchking.art_of_forging.effects.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class DevouringEffect {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(devouringEffect, 1);
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, devouringEffectName, 0, 30, false, effectStatGetter,
                        LabelGetterBasic.integerLabel, new TooltipGetterInteger
                        (devouringEffectTooltip, effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingIncomingDamageEvent event) {
        Entity attackingEntity = event.getSource().getEntity();

        if (attackingEntity instanceof LivingEntity attacker) {
            ItemStack heldStack = attacker.getMainHandItem();

            if (heldStack.getItem() instanceof ModularItem item) {
                // Effect
                int level = item.getEffectLevel(heldStack, devouringEffect);

                if (level > 0 && !attacker.level().isClientSide() && attacker instanceof Player player) {
                    // System.out.println("Reset!");

                    PlayerDevouringProvider.get(player).ifPresent
                            (PlayerDevouring::resetDevour);

                    // System.out.println("Removing effect..." + PotionEffects.DEVOURING);
                    player.removeEffect(PotionEffects.DEVOURING);
                }
            }
        }
    }

    // For Devouring
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        ItemStack heldStack = event.getEntity().getMainHandItem();

        // Every 5 seconds
        if (event.getEntity().tickCount % 100 == 0 && heldStack.getItem() instanceof ModularItem item) {
            // System.out.println("adding");

            // Potency of effect
            int level = item.getEffectLevel(heldStack, devouringEffect);

            // Duration of effect
            int eff = (int) item.getEffectEfficiency(heldStack, devouringEffect);

            if (level > 0 && !event.getEntity().hasEffect(PotionEffects.DEVOURING) && !event.getEntity().level().isClientSide()) {
                PlayerDevouringProvider.get(event.getEntity()).ifPresent(devouring
                        -> {
                    // System.out.println("current level "+ devouring.getDevour());

                    devouring.addDevour(1);

                    if (devouring.getDevour() >= 30) {
                        // System.out.println("Adding effect..." + PotionEffects.DEVOURING);

                        event.getEntity().addEffect(new MobEffectInstance(PotionEffects.DEVOURING,
                                eff * 20, level, true, true, true));
                    }
                });
            } else if (heldStack.getItem() != item && event.getEntity().hasEffect(PotionEffects.DEVOURING) && !event.getEntity().level().isClientSide()) {
                // System.out.println("Reset!");

                PlayerDevouringProvider.get(event.getEntity()).ifPresent
                        (PlayerDevouring::resetDevour);

                // System.out.println("Removing effect..." + PotionEffects.DEVOURING);
                event.getEntity().removeEffect(PotionEffects.DEVOURING);
            }
        }
    }

}
