package teamProject.view;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import teamProject.controller.GameController;
import teamProject.controller.GameEventListener;
import teamProject.util.AssetLoader;
import teamProject.util.SaveManager;
import teamProject.view.panels.GamePanel;
import teamProject.view.panels.GamePanelBuilder;
import teamProject.view.panels.HudPanel;
import teamProject.view.panels.LevelSelectPanel;
import teamProject.view.panels.LosePanel;
import teamProject.view.panels.MenuPanel;
import teamProject.view.panels.PausePanel;
import teamProject.view.panels.WinPanel;

public class GameView extends JFrame implements KeyListener, GameEventListener {
    private final GameController controller;
    private final Set<Character> heldKeys = ConcurrentHashMap.newKeySet();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private int currentLevel;

    // --- Panel references ---

    private final MenuPanel menuPanel;
    private HudPanel hudPanel;
    private GamePanel gamePanel;
    private WinPanel winPanel;
    private LosePanel losePanel;
    private PausePanel pausePanel;
    private LevelSelectPanel levelSelectPanel;

    // --- Lifecycle ---

    public GameView(GameController controller) {
        super("The Blind Mice");
        this.controller = controller;
        currentLevel = 1;
        AssetLoader.loadLetterAssets();

        // Build game rendering scene
        this.hudPanel = new HudPanel(AssetLoader.loadRedHeart(32), AssetLoader.loadBlackHeart(32));
        this.gamePanel = new GamePanelBuilder()
                .tileMap(controller.getTileMap())
                .player(controller.getPlayer())
                .backgroundImage(AssetLoader.loadBackground(controller.getLevel()))
                .wallImage(AssetLoader.loadWall())
                .destructibleImage(AssetLoader.loadDestructibleWall())
                .spikeImage(AssetLoader.loadSpike())
                .mouseLeft(AssetLoader.loadMouseLeft())
                .mouseRight(AssetLoader.loadMouseRight())
                .enemies(controller.getEnemies())
                .enemySprite(AssetLoader.loadEnemySnake())
                .hudPanel(this.hudPanel)
                .keyListener(this)
                .build();

        // Build menu/overlay panels with routing lambdas
        menuPanel = new MenuPanel(e -> showGame(1), e -> showLevelSelect());
        this.winPanel = new WinPanel(e -> showGame(currentLevel + 1), e -> showGame(currentLevel), e -> showMenu());
        this.losePanel = new LosePanel(e -> showGame(currentLevel), e -> showMenu());
        this.pausePanel = new PausePanel(e -> resumeGame(), e -> showMenu());
        this.levelSelectPanel = new LevelSelectPanel(this::showGame, e -> showMenu());

        container.add(menuPanel, "MENU");
        container.add(gamePanel, "GAME");
        container.add(winPanel, "WIN");
        container.add(losePanel, "LOSE");
        container.add(pausePanel, "PAUSE");
        container.add(levelSelectPanel, "LEVEL_SELECT");
        setContentPane(container);
        cardLayout.show(container, "MENU");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Navigation ---

    private void showGame(int level) {
        heldKeys.clear();
        currentLevel = level;
        controller.setLevel(level);
        controller.reset();
        gamePanel.setBackgroundImage(AssetLoader.loadBackground(level));
        gamePanel.updateRenderData(controller.getTileMap(), controller.getEnemies());
        cardLayout.show(container, "GAME");
        gamePanel.requestFocusInWindow();
    }

    private void showMenu() {
        heldKeys.clear();
        controller.resume();
        menuPanel.refresh(SaveManager.getHighScore());
        cardLayout.show(container, "MENU");
    }

    private void showLevelSelect() {
        levelSelectPanel.refresh(SaveManager.getMaxUnlockedLevel());
        cardLayout.show(container, "LEVEL_SELECT");
    }

    private void resumeGame() {
        controller.resume();
        cardLayout.show(container, "GAME");
        gamePanel.requestFocusInWindow();
    }

    // --- GameEventListener ---

    @Override
    public void onRenderRequested() {
        gamePanel.repaint();
    }

    @Override
    public void onPlayerDirectionChanged(char direction) {
        gamePanel.setMouseDirection(direction);
    }

    @Override
    public void onLetterBankChanged(List<Character> letters) {
        hudPanel.updateLetterBank(letters);
    }

    @Override
    public void onLevelComplete(long score, int health, long timeElapsedMs) {
        heldKeys.clear();
        winPanel.setNextLevelAvailable(currentLevel < 3);
        winPanel.setFinalScore(score);
        winPanel.setRemainingHealth(health);
        winPanel.setTimeElapsed(timeElapsedMs);
        cardLayout.show(container, "WIN");
    }

    @Override
    public void onGameOver(long score) {
        heldKeys.clear();
        losePanel.setScore(score);
        cardLayout.show(container, "LOSE");
    }

    @Override
    public void onGamePaused() {
        heldKeys.clear();
        cardLayout.show(container, "PAUSE");
    }

    @Override
    public void onGameResumed() {
        resumeGame();
    }

    @Override
    public void onScoreChanged(long score) {
        hudPanel.updateScore(score);
    }

    @Override
    public void onHealthChanged(int health) {
        hudPanel.updateHealth(health);
    }

    @Override
    public void onPowerChanged(String name, int ticksRemaining) {
        hudPanel.updatePower(name, ticksRemaining);
    }

    @Override
    public void onTimeChanged(long elapsedMs) {
        hudPanel.updateTimer(elapsedMs);
    }

    @Override
    public Set<Character> getHeldKeys() {
        return heldKeys;
    }

    // --- KeyListener ---

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            controller.pause();
            onGamePaused();
            return;
        }
        char key = Character.toUpperCase(e.getKeyChar());
        heldKeys.add(key);
        controller.processInput(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        heldKeys.remove(Character.toUpperCase(e.getKeyChar()));
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
