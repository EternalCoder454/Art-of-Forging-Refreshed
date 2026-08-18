package net.acetheeldritchking.art_of_forging.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import top.theillusivec4.curios.api.CuriosApi;

import static net.acetheeldritchking.art_of_forging.effects.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class ResolveEffect {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(resolveEffect, 1);
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, resolveName, 0, 30, false, effectStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterMultiValue
                        (resolveTooltip, StatsHelper.withStats
                                (effectStatGetter, new StatGetterEffectEfficiency
                                        (resolveEffect, 1.0D)), StatsHelper.withFormat
                                (StatFormat.noDecimal, StatFormat.noDecimal)));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onPlayerTickEvent(PlayerTickEvent.Post event) {
        ItemStack heldStack = event.getEntity().getMainHandItem();

        if (heldStack.getItem() instanceof ModularItem item) {
            // Duration of potion
            int level = item.getEffectLevel(heldStack, resolveEffect);

            // Hearts left
            float eff = item.getEffectEfficiency(heldStack, resolveEffect);

            // Attacker health
            float health = event.getEntity().getHealth();

            if (level > 0 && eff >= health) {
                applyEffects(level, event.getEntity());
            }
        }

        // For curio effect
        Player player = event.getEntity();

        // Finds curio and applies effect
        CuriosApi.getCuriosInventory(player).ifPresent(inv -> inv.findCurios(itemStack ->
                itemStack.getItem() instanceof ModularItem).forEach
                (slotResult -> {
                    slotResult.stack();

                    if (event.getEntity().tickCount % 20 == 0)
                    {
                        ItemStack itemStack = slotResult.stack();
                        ModularItem item = (ModularItem) itemStack.getItem();

                        // Duration of potion
                        int level = item.getEffectLevel(itemStack, resolveEffect);

                        // Hearts left
                        float eff = item.getEffectEfficiency(itemStack, resolveEffect);

                        // Player health
                        float health = player.getHealth();

                        if (level > 0 && eff >= health)
                        {
                            applyEffects(level, player);
                        }
                    }
                }));
    }

    private void applyEffects(int duration, LivingEntity user) {
        user.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, duration * 20,
                1, true, true, true));
        user.addEffect(new MobEffectInstance(MobEffects.STRENGTH, duration * 20,
                1, true, true, true));
        user.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, duration * 20,
                1, true, true, true));
    }
}
