package net.acetheeldritchking.art_of_forging.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * Adds a fixed set of stacks to a loot table.
 *
 * <p><b>This never worked, in this fork or upstream.</b> The codec read a field named {@code item}
 * holding one item, while every one of the data files that uses it writes {@code items} holding a
 * list. So all of them failed to parse and were dropped, and none of the loot they describe has ever
 * been obtainable. It reads the list now, which is what the data has always said.
 *
 * <p>It adds every stack listed rather than choosing between them. That is what the author's own
 * data says he meant: where he wanted a roll he used a separate loot table and the modifier that
 * rolls one, which is six of the nine files here. This one is for adding specific things.
 *
 * <p>The entries are stacks rather than bare item ids, so one can carry components. A dungeon chest
 * gets a scroll with its contents on it, which a plain item could not express.
 */
public class AddItemModifier extends LootModifier {
    public static final Supplier<MapCodec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(ItemStack.CODEC.listOf().fieldOf("items").forGetter(m -> m.items))
            .apply(inst, AddItemModifier::new)));

    private final List<ItemStack> items;

    /**
     * A modifier carries a priority now, which decides the order modifiers run in, and codecStart
     * writes it, so it is both an argument here and a field in the json.
     */
    public AddItemModifier(LootItemCondition[] conditionsIn, int priority, List<ItemStack> items) {
        super(conditionsIn, priority);
        this.items = List.copyOf(items);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        for (ItemStack stack : items) {
            // a copy, because the parsed stacks are shared by every roll of this table
            generatedLoot.add(stack.copy());
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
