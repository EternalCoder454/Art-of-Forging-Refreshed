package net.acetheeldritchking.art_of_forging.effects;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static net.acetheeldritchking.art_of_forging.effects.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class JudgementEffect {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(judgementEffect, 1);
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, judgementName, 0, 30, false, effectStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterMultiValue
                        (judgementTooltip, StatsHelper.withStats
                                (effectStatGetter, new StatGetterEffectEfficiency
                                        (judgementEffect, 1.0D)), StatsHelper.withFormat
                                (StatFormat.oneDecimal, StatFormat.oneDecimal)));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingIncomingDamageEvent event) {
        Entity attackingEntity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();

        if (attackingEntity instanceof LivingEntity attacker) {
            ItemStack heldStack = attacker.getMainHandItem();

            if (heldStack.getItem() instanceof ModularItem item) {
                // Percentage of health
                float level = item.getEffectLevel(heldStack, judgementEffect);

                // Chance to proc
                float eff = item.getEffectEfficiency(heldStack, judgementEffect);

                // Target health
                // Base health, constant
                final float BASE_HEALTH = target.getMaxHealth();
                // Base health, not constant
                float baseHealth = target.getHealth();
                // health as a percentage
                double healthPercentage = (baseHealth / BASE_HEALTH) * 100;

                if (level > 0 && eff > (target.getRandom().nextFloat() * 100) && level >= healthPercentage) {
                    //System.out.println("Killed");
                    if (target.level() instanceof ServerLevel serverLevel) {
                        target.kill(serverLevel);
                    }
                }
            }
        }
    }
}
