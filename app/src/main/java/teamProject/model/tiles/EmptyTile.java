package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public class EmptyTile implements Tile {
    public boolean isSolid() {
        return false;
    }

    public void onPlayerContact(PlayerEntity player) {}
}
