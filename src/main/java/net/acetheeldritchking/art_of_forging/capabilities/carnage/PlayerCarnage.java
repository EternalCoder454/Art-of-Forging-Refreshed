package net.acetheeldritchking.art_of_forging.capabilities.carnage;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class PlayerCarnage implements ValueIOSerializable {
    private int carnage;
    private final int MAX_CARNAGE = 5;
    private final int MIN_CARNAGE = 0;

    public int getCarnage() {
        return carnage;
    }

    public int setCarnage(int set) {
        this.carnage = set;
        return carnage;
    }

    public void resetCarnage() {
        this.carnage = 0;
    }

    public void addCarnage(int add) {
        this.carnage = Math.min(carnage + add, MAX_CARNAGE);
    }

    public void subCarnage(int sub) {
        this.carnage = Math.max(carnage - sub, MIN_CARNAGE);
    }

    public void copyFrom(PlayerCarnage source) {
        this.carnage = source.carnage;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("carnage", carnage);
    }

    @Override
    public void deserialize(ValueInput input) {
        carnage = input.getIntOr("carnage", 0);
    }
}
