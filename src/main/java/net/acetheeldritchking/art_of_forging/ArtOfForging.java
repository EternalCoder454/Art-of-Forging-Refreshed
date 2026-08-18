package net.acetheeldritchking.art_of_forging;

import com.mojang.logging.LogUtils;
import net.acetheeldritchking.art_of_forging.effects.*;
import net.acetheeldritchking.art_of_forging.effects.curio.*;
import net.acetheeldritchking.art_of_forging.effects.gui.EffectGuiStats;
import net.acetheeldritchking.art_of_forging.effects.potion.PotionEffects;
import net.acetheeldritchking.art_of_forging.loot.ModLootModifiers;
import net.acetheeldritchking.art_of_forging.capabilities.AoFAttachments;
import net.acetheeldritchking.art_of_forging.item.AoFCreativeModeTab;
import net.acetheeldritchking.art_of_forging.networking.AoFPackets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ArtOfForging.MOD_ID)
public class ArtOfForging {
    public static final String MOD_ID = "art_of_forging";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArtOfForging(IEventBus bus) {
        bus.addListener(this::commonSetup);
        bus.addListener(AoFPackets::register);
        if (FMLEnvironment.getDist().isClient()) {
            bus.addListener(ClientModEvents::onClientSetup);
        }

        // Items //
        AoFRegistry.ITEMS.register(bus);

        // The creative tab, which was never registered before //
        AoFCreativeModeTab.register(bus);

        // Loot Tables //
        ModLootModifiers.register(bus);

        // Potion Effects //
        PotionEffects.register(bus);

        // The six per player values, which were capabilities and are attachments now //
        AoFAttachments.ATTACHMENT_TYPES.register(bus);

        // Item Effects //
        // Withering
        NeoForge.EVENT_BUS.register(new WitheringEffect());
        // Storm Caller
        NeoForge.EVENT_BUS.register(new StormCallerEffect());
        // Evoking Maw
        NeoForge.EVENT_BUS.register(new EvokingMawEffect());
        // Life Steal
        NeoForge.EVENT_BUS.register(new LifeStealEffect());
        // Knockback
        NeoForge.EVENT_BUS.register(new KnockbackEffect());
        // Life Fiber Loss
        NeoForge.EVENT_BUS.register(new LifeFiberLossEffect());
        // Cavalry
        NeoForge.EVENT_BUS.register(new CavalryEffect());
        // Dismounting
        NeoForge.EVENT_BUS.register(new DismountingEffect());
        // Dragon Mist
        NeoForge.EVENT_BUS.register(new DragonMistEffect());
        // Disorienting
        NeoForge.EVENT_BUS.register(new DisorientingEffect());
        // Vengeance
        NeoForge.EVENT_BUS.register(new VengeanceEffect());
        // Resolve
        NeoForge.EVENT_BUS.register(new ResolveEffect());
        // Devouring
        NeoForge.EVENT_BUS.register(new DevouringEffect());
        // Infernal Rebuke
        NeoForge.EVENT_BUS.register(new InfernalRebukeEffect());
        // Hubris
        NeoForge.EVENT_BUS.register(new HubrisEffect());
        // Slaughtering
        NeoForge.EVENT_BUS.register(new SlaughteringEffect());
        // Carnage
        NeoForge.EVENT_BUS.register(new CarnageEffect());
        // Judgement
        NeoForge.EVENT_BUS.register(new JudgementEffect());
        // Retribution
        NeoForge.EVENT_BUS.register(new RetributionEffect());
        // Esoteric Edge
        NeoForge.EVENT_BUS.register(new EsotericEdgeEffect());
        // Feasting
        NeoForge.EVENT_BUS.register(new FeastingEffect());
        // Nano-Fused
        NeoForge.EVENT_BUS.register(new NanoFusedEffect());
        // Beheading
        NeoForge.EVENT_BUS.register(new BeheadingEffect());
        // Soul Charged
        NeoForge.EVENT_BUS.register(new SoulChargedEffect());
        // Sonic Shock
        NeoForge.EVENT_BUS.register(new SonicShockEffect());
        // Conquering
        NeoForge.EVENT_BUS.register(new ConqueringEffect());
        // Subjugation
        NeoForge.EVENT_BUS.register(new SubjugationEffect());
        // Goliath Slayer
        NeoForge.EVENT_BUS.register(new GoliathSlayerEffect());

        // Curio Effects //
        // Flame Protection
        NeoForge.EVENT_BUS.register(new CurioFireResistanceEffect());
        // Arcane Protection
        NeoForge.EVENT_BUS.register(new CurioMagicResistanceEffect());
        // Strength Infused
        NeoForge.EVENT_BUS.register(new CurioStrengthEffect());
        // Healing Infused
        NeoForge.EVENT_BUS.register(new CurioRegenerationEffect());
        // Karma Infused
        NeoForge.EVENT_BUS.register(new CurioKarmaEffect());
        // Haste Infused
        NeoForge.EVENT_BUS.register(new CurioHasteEffect());
        // Third Sight
        NeoForge.EVENT_BUS.register(new CurioGlowingEffect());

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // The three charged abilities this mod adds to every handheld modular item. Tetra takes
        // registrations for these now, so it no longer needs a mixin into its static initialiser.
        event.enqueueWork(EffectGuiStats::setupAbilites);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    public static class ClientModEvents {
        public static void onClientSetup(FMLClientSetupEvent event) {
            WitheringEffect.init();
            StormCallerEffect.init();
            EvokingMawEffect.init();
            LifeStealEffect.init();
            KnockbackEffect.init();
            LifeFiberLossEffect.init();
            CavalryEffect.init();
            DismountingEffect.init();
            DragonMistEffect.init();
            DisorientingEffect.init();
            VengeanceEffect.init();
            ResolveEffect.init();
            DevouringEffect.init();
            InfernalRebukeEffect.init();
            CarnageEffect.init();
            SlaughteringEffect.init();
            HubrisEffect.init();
            JudgementEffect.init();
            RetributionEffect.init();
            EsotericEdgeEffect.init();
            FeastingEffect.init();
            NanoFusedEffect.init();
            BeheadingEffect.init();
            SoulChargedEffect.init();
            SonicShockEffect.init();
            ConqueringEffect.init();
            SubjugationEffect.init();
            GoliathSlayerEffect.init();
            // Curio
            CurioFireResistanceEffect.init();
            CurioMagicResistanceEffect.init();
            CurioStrengthEffect.init();
            CurioRegenerationEffect.init();
            CurioKarmaEffect.init();
            CurioHasteEffect.init();
            CurioGlowingEffect.init();
        }
    }
}
