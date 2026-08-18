package net.acetheeldritchking.art_of_forging.item.modular;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ModularItem;
import top.theillusivec4.curios.api.type.capability.ICurio;


public class ModularArtifact extends ModularItem implements ICurio {
    public final static String artifactCasing = "artifact/casing";
    public final static String artifactInternal = "artifact/internal";

    public final static String artifactAttachment = "artifact/attachment";

    public static final String identifier = "modular_artifact";

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(-13, -1, 3, 19);
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(6, 1);

    public static ModularArtifact instance;

    public ModularArtifact(Properties properties) {
        super(properties.stacksTo(1).fireResistant());

        canHone = false;

        majorModuleKeys = new String[]{artifactCasing, artifactInternal};
        minorModuleKeys = new String[]{artifactAttachment};

        requiredModules = new String[]{artifactCasing, artifactInternal};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }

    @Override
    public ItemStack getStack() {
        return null;
    }

    /**
     * {@return a working artifact, for the creative tabs}
     *
     * <p>Both required slots are filled, because an artifact missing either is an empty casing that
     * does nothing and reads as broken. Iron and redstone are the plainest material each slot takes.
     */
    public static ItemStack setupArtifact() {
        ItemStack itemStack = new ItemStack(instance);

        IModularItem.putModuleInSlot(itemStack, artifactCasing, "artifact/casing/relic", "relic/iron");
        IModularItem.putModuleInSlot(itemStack, artifactInternal, "artifact/internal/orb", "orb/redstone_dust");
        IModularItem.updateIdentifier(itemStack);

        return itemStack;
    }
}
