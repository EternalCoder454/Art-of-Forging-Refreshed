package net.acetheeldritchking.art_of_forging.capabilities.devouring;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class PlayerDevouring implements ValueIOSerializable {
    private int devour;
    private final int MAX_DEVOUR = 30;
    private final int MIN_DEVOUR = 0;

    public int getDevour() {
        return devour;
    }

    public void addDevour(int add) {
        this.devour = Math.min(devour + add, MAX_DEVOUR);
    }

    public void subDevour(int sub) {
        this.devour = Math.max(devour - sub, MIN_DEVOUR);
    }

    public int setDevour(int set) {
        this.devour = set;
        return devour;
    }

    public void resetDevour() {
        this.devour = 0;
    }

    public void copyFrom(PlayerDevouring source) {
        this.devour = source.devour;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("devouring", devour);
    }

    @Override
    public void deserialize(ValueInput input) {
        devour = input.getIntOr("devouring", 0);
    }
}
