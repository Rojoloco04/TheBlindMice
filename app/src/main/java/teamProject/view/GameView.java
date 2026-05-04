package teamProject.view;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import teamProject.controller.GameController;
import teamProject.controller.GameEventListener;

public class GameView extends JFrame implements KeyListener, GameEventListener {
    private final GameController controller;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private HudPanel hudPanel;
    private GamePanel gamePanel;
    private WinPanel winPanel;

    public GameView(GameController controller) {
        super("The Blind Mice");
        this.controller = controller;

        GameSceneFactory.GameScene scene = GameSceneFactory.buildGameScene(
            controller,
            this,
            e -> showGame(),  // ActionListener: start button clicked
            e -> showMenu()   // ActionListener: menu button clicked
        );

        this.hudPanel = scene.hudPanel;
        this.gamePanel = scene.gamePanel;
        this.winPanel = scene.winPanel;

        container.add(scene.menuPanel, "MENU");
        container.add(gamePanel, "GAME");
        container.add(scene.winPanel, "WIN");
        setContentPane(container);
        cardLayout.show(container, "MENU");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showGame() {
        cardLayout.show(container, "GAME");
        gamePanel.requestFocusInWindow();
    }

    private void showMenu() {
        cardLayout.show(container, "MENU");
    }

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
    public void onLevelComplete(long score, int health) {
        if (winPanel != null) {
            winPanel.setFinalScore(score);
            winPanel.setRemainingHealth(health);
        }
        cardLayout.show(container, "WIN");
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
    public void keyPressed(KeyEvent e) {
        char key = Character.toUpperCase(e.getKeyChar());
        controller.processInput(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
