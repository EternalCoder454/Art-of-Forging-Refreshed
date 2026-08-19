package net.acetheeldritchking.art_of_forging.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.acetheeldritchking.art_of_forging.item.modular.ModularArtifact;
import se.mickelus.tetra.TetraMod;

/**
 * What this mod adds to EMI, which is less than it sounds.
 *
 * <p>Its materials are already there. Tetra reads every material in the store rather than only its
 * own, and this mod's twenty two ship under {@code data/tetra/materials} like everything else, so
 * they appear in Tetra's material category without either mod knowing about the other. Its twenty
 * three recipes are ordinary crafting recipes and EMI finds those by itself. Listing any of it
 * again here would show every entry twice.
 *
 * <p>What Tetra cannot know is that the modular artifact is somewhere materials go. Tetra names the
 * workbench, the hammer and the holosphere as workstations for that category, being the three
 * places a player is holding the question the page answers. The artifact is a fourth: its casing
 * takes metals and its internal takes reagents, and reagents are a category that exists only
 * because this mod adds it, so a player looking at a ghast tear in EMI has nowhere else to be sent.
 *
 * <p>EMI finds this by the annotation, so nothing else refers to it and a pack without EMI never
 * loads it. It compiles against EMI Refreshed, a fork, because upstream EMI stops at 1.21.1.
 */
@OnlyIn(Dist.CLIENT)
@EmiEntrypoint
public class AoFEmiPlugin implements EmiPlugin {

    /**
     * Tetra's own material category, named by id rather than read off its plugin, so this never
     * loads a Tetra class that would not be there if Tetra changed how it registers.
     */
    private static final EmiRecipeCategory materialCategory = new EmiRecipeCategory(
            Identifier.fromNamespaceAndPath(TetraMod.MOD_ID, "material"), EmiStack.EMPTY);

    @Override
    public void register(EmiRegistry registry) {
        if (ModularArtifact.instance == null) {
            return;
        }

        registry.addWorkstation(materialCategory, EmiStack.of(ModularArtifact.instance));
    }
}
