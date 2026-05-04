package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class GrowStrategy extends PowerStrategy {
    static final int GROW_SIZE = 64;

    @Override
    public void apply(PlayerEntity player) {
        player.setSize(GROW_SIZE, GROW_SIZE);
        player.setTileBreaker(true);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setSize(PlayerEntity.DEFAULT_WIDTH, PlayerEntity.DEFAULT_HEIGHT);
        player.setTileBreaker(false);
    }

    @Override
    public String getName() {
        return "GROW";
    }
}
