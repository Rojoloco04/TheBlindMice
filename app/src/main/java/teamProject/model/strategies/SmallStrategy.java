package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class SmallStrategy extends PowerStrategy {
    @Override
    public void apply(PlayerEntity player) {
        player.setSize(3);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setSize(PlayerEntity.DEFAULT_SIZE);
    }
}
