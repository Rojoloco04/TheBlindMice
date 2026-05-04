package teamProject.model.tiles;

import teamProject.model.PlayerEntity;

public class LetterTile implements Tile {
    private final char letter;
    private boolean collected;

    public LetterTile(char letter) {
        this.letter = letter;
        collected = false;
    }

    public boolean isSolid() {
        return false;
    }

    public void onPlayerContact(PlayerEntity player) {
        collected = true;
    }

    public char getLetter() {
        return letter;
    }

    public boolean isCollected() {
        return collected;
    }
}
