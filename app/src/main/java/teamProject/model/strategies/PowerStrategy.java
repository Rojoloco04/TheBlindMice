package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public abstract class PowerStrategy {
    public abstract void apply(PlayerEntity player);
    public abstract void remove(PlayerEntity player);
}
