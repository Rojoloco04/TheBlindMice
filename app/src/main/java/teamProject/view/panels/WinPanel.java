package teamProject.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class WinPanel extends BaseMenuPanel {
    private final JLabel scoreLabel;
    private final JLabel healthLabel;
    private final JLabel timeLabel;
    private final JButton nextLevelButton;

    // --- Lifecycle ---

    public WinPanel(ActionListener nextLevelAction, ActionListener retryAction, ActionListener mainMenuAction) {
        setLayout(null);
        setBackground(new Color(20, 50, 20));

        nextLevelButton = makeButton("NEXT LEVEL", 80, 530, new Color(50, 120, 200), 300, 80, 20);
        nextLevelButton.addActionListener(nextLevelAction);
        add(nextLevelButton);

        JButton retryButton = makeButton("RETRY", 490, 530, new Color(100, 150, 100), 300, 80, 20);
        retryButton.addActionListener(retryAction);
        add(retryButton);

        JButton menuButton = makeButton("MAIN MENU", 900, 530, new Color(60, 60, 60), 300, 80, 20);
        menuButton.addActionListener(mainMenuAction);
        add(menuButton);

        scoreLabel = new JLabel("FINAL SCORE: 0");
        scoreLabel.setBounds(150, 240, 980, 90);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        add(scoreLabel);

        healthLabel = new JLabel("HEARTS REMAINING: 0");
        healthLabel.setBounds(150, 340, 980, 70);
        healthLabel.setFont(new Font("Arial", Font.BOLD, 36));
        healthLabel.setForeground(new Color(220, 100, 100));
        healthLabel.setHorizontalAlignment(JLabel.CENTER);
        add(healthLabel);

        timeLabel = new JLabel("TIME: 0.0s");
        timeLabel.setBounds(150, 420, 980, 50);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(timeLabel);
    }

    // --- Updates ---

    public void setFinalScore(long score) {
        scoreLabel.setText("FINAL SCORE: " + score);
    }

    public void setRemainingHealth(int health) {
        healthLabel.setText("HEARTS REMAINING: " + health);
    }

    public void setTimeElapsed(long ms) {
        timeLabel.setText(String.format("TIME: %.1fs", ms / 1000.0));
    }

    public void setNextLevelAvailable(boolean available) {
        nextLevelButton.setVisible(available);
    }

    // --- Rendering ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(20, 50, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 64));
        g.setColor(Color.YELLOW);
        String title = "LEVEL COMPLETE!";
        int w = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - w) / 2, 200);
    }
}
