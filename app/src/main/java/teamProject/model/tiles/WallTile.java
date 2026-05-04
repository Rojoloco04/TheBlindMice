package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public class WallTile implements Tile {
    public boolean isSolid() {
        return true;
    }

    public void onPlayerContact(PlayerEntity player) {}
}
