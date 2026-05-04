package teamProject.model.strategies;

import teamProject.model.Coordinate;
import teamProject.model.PlayerEntity;

public class GrowStrategy extends PowerStrategy {
    static final int GROW_SIZE = 64;
    private static final int Y_SHIFT = GROW_SIZE - PlayerEntity.DEFAULT_HEIGHT;

    @Override
    public void apply(PlayerEntity player) {
        player.setSize(GROW_SIZE, GROW_SIZE);
        player.setTileBreaker(true);
        Coordinate pos = player.getPos();
        player.updatePos(new Coordinate(pos.getX(), pos.getY() - Y_SHIFT));
    }

    @Override
    public void remove(PlayerEntity player) {
        player.setSize(PlayerEntity.DEFAULT_WIDTH, PlayerEntity.DEFAULT_HEIGHT);
        player.setTileBreaker(false);
        Coordinate pos = player.getPos();
        player.updatePos(new Coordinate(pos.getX(), pos.getY() + Y_SHIFT));
    }

    @Override
    public String getName() {
        return "GROW";
    }
}
