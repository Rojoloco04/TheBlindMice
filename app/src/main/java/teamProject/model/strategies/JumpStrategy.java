package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class JumpStrategy extends PowerStrategy {
    @Override
    public void apply(PlayerEntity player) {
        player.setJumpHeight(2.0);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setJumpHeight(PlayerEntity.DEFAULT_JUMP_HEIGHT);
    }
}
