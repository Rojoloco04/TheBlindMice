package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class SmallStrategy extends PowerStrategy {
    static final int SMALL_SIZE = 16;

    @Override
    public void apply(PlayerEntity player) {
        player.setSize(SMALL_SIZE);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setSize(PlayerEntity.DEFAULT_WIDTH, PlayerEntity.DEFAULT_HEIGHT);
    }

    @Override
    public String getName() {
        return "SMALL";
    }
}
