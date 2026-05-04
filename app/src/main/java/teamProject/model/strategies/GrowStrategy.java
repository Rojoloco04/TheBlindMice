package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class GrowStrategy extends PowerStrategy {
    @Override
    public void apply(PlayerEntity player) {
        player.setSize(10);
        player.setTileBreaker(true);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setSize(PlayerEntity.DEFAULT_SIZE);
        player.setTileBreaker(false);
    }
}
