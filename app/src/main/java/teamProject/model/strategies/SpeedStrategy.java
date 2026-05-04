package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class SpeedStrategy extends PowerStrategy {
    @Override
    public void apply(PlayerEntity player) {
        player.setMoveSpeed(1.5);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setMoveSpeed(PlayerEntity.DEFAULT_MOVE_SPEED);
    }
}
