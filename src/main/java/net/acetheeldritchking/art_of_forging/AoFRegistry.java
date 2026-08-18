package net.acetheeldritchking.art_of_forging;

///import net.acetheeldritchking.art_of_forging.item.DummyItem;
import net.acetheeldritchking.art_of_forging.item.custom.AncientItem;
import net.acetheeldritchking.art_of_forging.item.custom.EnigmaticConstructItem;
import net.acetheeldritchking.art_of_forging.item.custom.LifeFiberItem;
import net.acetheeldritchking.art_of_forging.item.custom.SigilOfEdenItem;
import net.acetheeldritchking.art_of_forging.item.modular.ModularArtifact;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AoFRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArtOfForging.MOD_ID);

  ///  public static final DeferredHolder<Item, Item> DUMMY_ITEM = ITEMS.registerItem("dummyitem", DummyItem::new);
    //               //
    // Modular Items //
    //               //
    // Modular Artifact
    public static final DeferredHolder<Item, ModularArtifact> MODULAR_ARTIFACT =
            ITEMS.registerItem(ModularArtifact.identifier, properties -> {
                ModularArtifact item = new ModularArtifact(properties);
                ModularArtifact.instance = item;
                return item;
            });

    public static final DeferredHolder<Item, Item> CURIOUS_ARTIFACT = ITEMS.registerItem("curious_artifact",
            properties -> new Item(properties));


    //        //
    // INGOTS //
    //        //
    // Resonant Alloy
    public static final DeferredHolder<Item, Item> RESONANT_ALLOY = ITEMS.registerItem("resonant_alloy",
            properties -> new Item(properties.fireResistant()));

    // Forged Steel
    public static final DeferredHolder<Item, Item> FORGED_STEEL_INGOT = ITEMS.registerItem("forged_steel_ingot",
            properties -> new Item(properties.fireResistant()));

    // Vobrite Crystal
    public static DeferredHolder<Item, Item> VOBRITE_CRYSTAL = ITEMS.registerItem("vobrite_crystal",
            properties -> new Item(properties.rarity(Rarity.UNCOMMON).
                    fireResistant()));

    // Vobrivium Ingot
    public static DeferredHolder<Item, Item> VOBRIVIUM_INGOT = ITEMS.registerItem("vobrivium_ingot",
            properties -> new Item(properties.rarity(Rarity.UNCOMMON).
                    fireResistant()));

    // Endsteel
    public static DeferredHolder<Item, Item> ENDSTEEL_INGOT = ITEMS.registerItem("endsteel_ingot",
            properties -> new Item(properties.
                    rarity(Rarity.UNCOMMON).fireResistant()));


    //          //
    // TREASURE //
    //          //
    // Life Fiber
    public static DeferredHolder<Item, Item> LIFE_FIBER = ITEMS.registerItem("life_fiber",
            properties -> new LifeFiberItem(properties.rarity(Rarity.RARE)));

    // Fang Charm
    public static final DeferredHolder<Item, Item> FANG_CHARM = ITEMS.registerItem("fang_charm",
            properties -> new Item(properties.rarity(Rarity.RARE).stacksTo(1)));

    // Sigil of Eden
    public static DeferredHolder<Item, Item> SIGIL_OF_EDEN = ITEMS.registerItem("sigil_of_eden",
            properties -> new SigilOfEdenItem(properties.
                    rarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    // Enigmatic Construct
    public static DeferredHolder<Item, Item> ENIGMATIC_CONSTRUCT = ITEMS.registerItem("enigmatic_construct",
            properties -> new EnigmaticConstructItem(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Ancient Axe head
    public static DeferredHolder<Item, Item> ANCIENT_AXE = ITEMS.registerItem("ancient_axe",
            properties -> new AncientItem(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Ancient Blade
    public static DeferredHolder<Item, Item> ANCIENT_BLADE = ITEMS.registerItem("ancient_blade",
            properties -> new AncientItem(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Ancient Flail
    public static DeferredHolder<Item, Item> ANCIENT_FLAIL = ITEMS.registerItem("ancient_flail",
            properties -> new AncientItem(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Demonic Axe Head
    public static DeferredHolder<Item, Item> DEMONIC_AXE = ITEMS.registerItem("demonic_axe",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Demonic Blade
    public static DeferredHolder<Item, Item> DEMONIC_BLADE = ITEMS.registerItem("demonic_blade",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Demonic Flail
    public static DeferredHolder<Item, Item> DEMONIC_FLAIL = ITEMS.registerItem("demonic_flail",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    // Devil's Soul Gem
    public static DeferredHolder<Item, Item> DEVILS_SOUL_GEM = ITEMS.registerItem("devils_soul_gem",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    // Rending Scissor Red
    public static DeferredHolder<Item, Item> RENDING_SCISSOR_RED = ITEMS.registerItem("rending_scissor_red",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    // Rending Scissor Purple
    public static DeferredHolder<Item, Item> RENDING_SCISSOR_PURPLE = ITEMS.registerItem("rending_scissor_purple",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    // Rending Scissor Complete
    public static DeferredHolder<Item, Item> RENDING_SCISSOR_COMPLETE = ITEMS.registerItem("rending_scissor_complete",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).stacksTo(1).fireResistant()));


    //      //
    // MISC //
    //      //
    // Nano-Insectoid
    public static DeferredHolder<Item, Item> NANO_INSECTOID = ITEMS.registerItem("nano_insectoid",
            properties -> new Item(properties.
                    rarity(Rarity.UNCOMMON).stacksTo(16)));

    // Encoded Canister
    public static DeferredHolder<Item, Item> ENCODED_CANISTER = ITEMS.registerItem("encoded_canister",
            properties -> new Item(properties.
                    rarity(Rarity.UNCOMMON).stacksTo(16)));

    // Esoteric Codex
    public static DeferredHolder<Item, Item> ESOTERIC_CODEX = ITEMS.registerItem("esoteric_codex",
            properties -> new Item(properties.
                    rarity(Rarity.RARE).stacksTo(1)));

    // Mark of The Architect
    public static DeferredHolder<Item, Item> MARK_OF_THE_ARCHITECT = ITEMS.registerItem("mark_of_the_architect",
            properties -> new Item(properties.
                    rarity(Rarity.EPIC).stacksTo(1)));

    // Shockwave Chamber
    public static DeferredHolder<Item, Item> SHOCKWAVE_CHAMBER = ITEMS.registerItem("shockwave_chamber",
            properties -> new Item(properties.
                    rarity(Rarity.RARE).stacksTo(16)));


    //           //
    // MOB DROPS //
    //           //
    // Dragon Soul
    public static DeferredHolder<Item, Item> DRAGON_SOUL = ITEMS.registerItem("dragon_soul",
            properties -> new Item(properties.
                    rarity(Rarity.RARE).fireResistant()));

    // Shards of Malice
    public static DeferredHolder<Item, Item> SHARDS_OF_MALICE = ITEMS.registerItem("shards_of_malice",
            properties -> new Item(properties.
                    rarity(Rarity.UNCOMMON).fireResistant()));

    // Potent Mixture
    public static DeferredHolder<Item, Item> POTENT_MIXTURE = ITEMS.registerItem("potent_mixture",
            properties -> new Item(properties.
                    rarity(Rarity.RARE)));

    // Heart of Ender
    public static DeferredHolder<Item, Item> HEART_OF_ENDER = ITEMS.registerItem("heart_of_ender",
            properties -> new Item(properties.
                    rarity(Rarity.RARE)));

    // Eerie Shard
    public static DeferredHolder<Item, Item> EERIE_SHARD = ITEMS.registerItem("eerie_shard",
            properties -> new Item(properties.
                    rarity(Rarity.RARE).stacksTo(16)));

    // Soul Ember
    public static DeferredHolder<Item, Item> SOUL_EMBER = ITEMS.registerItem("soul_ember",
            properties -> new Item(properties));

    // Fragment of Eden
    public static DeferredHolder<Item, Item> FRAGMENT_OF_EDEN = ITEMS.registerItem("fragment_of_eden",
            properties -> new Item(properties.
                    rarity(Rarity.RARE).stacksTo(16)));


}
