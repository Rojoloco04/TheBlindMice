package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public interface Tile {
    boolean isSolid();
    void onPlayerContact(PlayerEntity player);
}
