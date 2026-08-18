package net.acetheeldritchking.art_of_forging.capabilities.karma;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class PlayerKarma implements ValueIOSerializable {
    private int karma;
    private final int MAX_KARMA = 5;
    private final int MIN_KARMA = -5;

    public int getKarma() {
        return karma;
    }

    public int setKarma(int set) {
        this.karma = set;
        return karma;
    }

    public void resetKarma() {
        this.karma = 0;
    }

    public void addKarma(int add) {
        this.karma = Math.min(karma + add, MAX_KARMA);
    }

    public void subKarma(int sub) {
        this.karma = Math.max(karma - sub, MIN_KARMA);
    }

    public void copyFrom(PlayerKarma source) {
        this.karma = source.karma;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("karma", karma);
    }

    @Override
    public void deserialize(ValueInput input) {
        karma = input.getIntOr("karma", 0);
    }
}
