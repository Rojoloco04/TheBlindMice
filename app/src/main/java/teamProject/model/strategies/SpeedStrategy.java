package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class SpeedStrategy extends PowerStrategy {
    static final double BOOSTED_MOVE_SPEED = 7.0;

    @Override
    public void apply(PlayerEntity player) {
        player.setMoveSpeed(BOOSTED_MOVE_SPEED);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setMoveSpeed(PlayerEntity.DEFAULT_MOVE_SPEED);
    }

    @Override
    public String getName() {
        return "SPEED";
    }
}
