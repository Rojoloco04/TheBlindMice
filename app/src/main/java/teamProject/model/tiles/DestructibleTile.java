package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public class DestructibleTile implements Tile {
    private boolean destroyed;

    public DestructibleTile() {
        destroyed = false;
    } 

    public boolean isSolid() {
        return !destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void onPlayerContact(PlayerEntity player) {
        if (player.canBreakTiles()) { // large player is able to destroy DestructibleTiles
            destroyed = true;
        }
    }
}
