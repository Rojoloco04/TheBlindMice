package teamProject.view.panels;

import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

import teamProject.model.EnemyEntity;
import teamProject.model.PlayerEntity;
import teamProject.model.tiles.TileMap;

public class GamePanelBuilder {
    TileMap tileMap;
    PlayerEntity player;
    List<EnemyEntity> enemies;

    HudPanel hudPanel;
    KeyListener keyListener;

    BufferedImage backgroundImage;
    BufferedImage wallImage;
    BufferedImage destructibleImage;
    BufferedImage spikeImage;
    BufferedImage mouseLeft;
    BufferedImage mouseRight;
    BufferedImage enemySprite;

    // --- Model ---

    public GamePanelBuilder tileMap(TileMap tileMap) {
        this.tileMap = tileMap;
        return this;
    }

    public GamePanelBuilder player(PlayerEntity player) {
        this.player = player;
        return this;
    }

    public GamePanelBuilder enemies(List<EnemyEntity> enemies) {
        this.enemies = enemies;
        return this;
    }

    // --- UI ---

    public GamePanelBuilder hudPanel(HudPanel hudPanel) {
        this.hudPanel = hudPanel;
        return this;
    }

    public GamePanelBuilder keyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
        return this;
    }

    // --- Sprites ---

    public GamePanelBuilder backgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        return this;
    }

    public GamePanelBuilder wallImage(BufferedImage wallImage) {
        this.wallImage = wallImage;
        return this;
    }

    public GamePanelBuilder destructibleImage(BufferedImage destructibleImage) {
        this.destructibleImage = destructibleImage;
        return this;
    }

    public GamePanelBuilder spikeImage(BufferedImage spikeImage) {
        this.spikeImage = spikeImage;
        return this;
    }

    public GamePanelBuilder mouseLeft(BufferedImage mouseLeft) {
        this.mouseLeft = mouseLeft;
        return this;
    }

    public GamePanelBuilder mouseRight(BufferedImage mouseRight) {
        this.mouseRight = mouseRight;
        return this;
    }

    public GamePanelBuilder enemySprite(BufferedImage enemySprite) {
        this.enemySprite = enemySprite;
        return this;
    }

    // --- Build ---

    public GamePanel build() {
        return new GamePanel(this);
    }
}
