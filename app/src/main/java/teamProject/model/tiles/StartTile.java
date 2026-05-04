package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public class StartTile implements Tile {
    public boolean isSolid() {
        return false;
    }

    public void onPlayerContact(PlayerEntity player) {}
}
