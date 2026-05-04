package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public class SpikeTile implements Tile {
    public boolean isSolid() {
        return false;
    }
    
    public void onPlayerContact(PlayerEntity player) {
        player.damage();
    }
}
