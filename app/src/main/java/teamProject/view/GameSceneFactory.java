package teamProject.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import teamProject.controller.GameController;
import teamProject.util.AssetLoader;

public class GameSceneFactory {

    public static class GameScene {
        public final GamePanel gamePanel;
        public final HudPanel hudPanel;
        public final MenuPanel menuPanel;
        public final WinPanel winPanel;

        GameScene(GamePanel gamePanel, HudPanel hudPanel, MenuPanel menuPanel, WinPanel winPanel) {
            this.gamePanel = gamePanel;
            this.hudPanel = hudPanel;
            this.menuPanel = menuPanel;
            this.winPanel = winPanel;
        }
    }

    public static GameScene buildGameScene(GameController controller, KeyListener keyListener, ActionListener onStart, ActionListener onMenu) {
        AssetLoader assets = new AssetLoader();

        HudPanel hudPanel = new HudPanel(assets.loadRedHeart(32), assets.loadBlackHeart(32));

        GamePanel gamePanel = new GamePanelBuilder()
                .tileMap(controller.getTileMap())
                .player(controller.getPlayer())
                .backgroundImage(assets.loadBackground(controller.getLevel()))
                .wallImage(assets.loadWall())
                .destructibleImage(assets.loadDestructibleWall())
                .spikeImage(assets.loadSpike())
                .mouseLeft(assets.loadMouseLeft())
                .mouseRight(assets.loadMouseRight())
                .enemies(controller.getEnemies())
                .enemySprite(assets.loadEnemySnake())
                .hudPanel(hudPanel)
                .keyListener(keyListener)
                .build();

        MenuPanel menuPanel = new MenuPanel(assets.loadMenuPanel(), onStart);

        // Create win panel with button listeners
        WinPanel winPanel = new WinPanel(
            assets.loadBackground(controller.getLevel()),
            e -> System.out.println("Play Again clicked"), // TODO: implement proper play again logic
            onMenu
        );

        return new GameScene(gamePanel, hudPanel, menuPanel, winPanel);
    }
}
