package net.acetheeldritchking.art_of_forging.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
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
 * holding one item, while every data file that uses it writes {@code items} holding a list, so all
 * of them failed to parse and none of the loot they describe was ever obtainable.
 *
 * <p>It adds every entry rather than choosing between them. That is what the author's own data says
 * he meant: where he wanted a roll he used a separate loot table and the modifier that rolls one,
 * which is six of the nine files here. This one is for adding specific things.
 *
 * <p><b>An entry is not an ItemStack, and that is the point.</b> A loot modifier is parsed during a
 * datapack reload, and item components are unbound for the whole of one, so anything that builds a
 * stack while parsing fails with "Item ... does not have components yet". Reading the item, the
 * count and the component patch separately costs nothing and defers the stack to
 * {@link #doApply}, which runs when loot is rolled and components are long since bound.
 */
public class AddItemModifier extends LootModifier {

    /**
     * One thing to add, as data rather than as a stack.
     *
     * <p>The shape matches a vanilla item stack in json, an {@code id} with an optional
     * {@code count} and optional {@code components}, so the data reads the same as it would
     * anywhere else. Only the timing of when it becomes a stack is different.
     */
    public record Entry(Holder<Item> item, int count, DataComponentPatch components) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("id").forGetter(Entry::item),
                Codec.INT.optionalFieldOf("count", 1).forGetter(Entry::count),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                        .forGetter(Entry::components)
        ).apply(inst, Entry::new));

        public ItemStack toStack() {
            return new ItemStack(item, count, components);
        }
    }

    public static final Supplier<MapCodec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(Entry.CODEC.listOf().fieldOf("items").forGetter(m -> m.items))
            .apply(inst, AddItemModifier::new)));

    private final List<Entry> items;

    /**
     * A modifier carries a priority now, which decides the order modifiers run in, and codecStart
     * writes it, so it is both an argument here and a field in the json.
     */
    public AddItemModifier(LootItemCondition[] conditionsIn, int priority, List<Entry> items) {
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

        for (Entry entry : items) {
            generatedLoot.add(entry.toStack());
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
