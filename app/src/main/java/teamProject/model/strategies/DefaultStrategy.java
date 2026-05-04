package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

// pure alias for default strategy
public class DefaultStrategy extends PowerStrategy {
    @Override
    public void apply(PlayerEntity player) {}

    @Override
    public void remove(PlayerEntity player) {}

    @Override
    public String getName() {
        return "";
    }
}
