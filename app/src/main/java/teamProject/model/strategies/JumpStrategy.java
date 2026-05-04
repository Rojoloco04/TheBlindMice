package teamProject.model.strategies;

import teamProject.model.PlayerEntity;

public class JumpStrategy extends PowerStrategy {
    static final double BOOSTED_JUMP_HEIGHT = 14.0;

    @Override
    public void apply(PlayerEntity player) {
        player.setJumpHeight(BOOSTED_JUMP_HEIGHT);
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setJumpHeight(PlayerEntity.DEFAULT_JUMP_HEIGHT);
    }

    @Override
    public String getName() {
        return "JUMP";
    }
}
