package teamProject.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MenuPanel extends BaseMenuPanel {
    private long displayedHighScore = 0;

    // --- Lifecycle ---

    public MenuPanel(ActionListener startAction, ActionListener levelSelectAction) {
        setLayout(null);
        setBackground(new Color(20, 20, 50));

        JButton startButton = makeButton("PLAY", 470, 380, new Color(60, 120, 60), 340, 80, 22);
        startButton.addActionListener(startAction);
        add(startButton);

        JButton levelSelectButton = makeButton("SELECT LEVEL", 470, 480, new Color(50, 80, 150), 340, 80, 22);
        levelSelectButton.addActionListener(levelSelectAction);
        add(levelSelectButton);

        JButton quitButton = makeButton("QUIT", 470, 580, new Color(120, 40, 40), 340, 80, 22);
        quitButton.addActionListener(e -> System.exit(0));
        add(quitButton);
    }

    // --- Updates ---

    public void refresh(long highScore) {
        displayedHighScore = highScore;
        repaint();
    }

    // --- Rendering ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(20, 20, 50));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 72));
        g.setColor(Color.WHITE);
        String title = "THE BLIND MICE";
        int tw = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - tw) / 2, 180);

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.YELLOW);
        String hs = "HIGH SCORE: " + displayedHighScore;
        int hw = g.getFontMetrics().stringWidth(hs);
        g.drawString(hs, (getWidth() - hw) / 2, 240);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(new Color(180, 180, 220));
        String sub = "A/D to move   W to jump   ESC to pause   Collect letters to form power words";
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (getWidth() - sw) / 2, 320);
    }
}
