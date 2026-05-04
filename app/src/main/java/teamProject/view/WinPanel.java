package teamProject.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WinPanel extends JPanel {
    private final BufferedImage backgroundImage;
    private JLabel scoreLabel;
    private JLabel healthLabel;
    private long finalScore;
    private int remainingHealth;

    public WinPanel(BufferedImage backgroundImage, ActionListener playAgainAction, ActionListener mainMenuAction) {
        this.backgroundImage = backgroundImage;
        this.finalScore = 0;
        this.remainingHealth = 0;
        
        setLayout(null);
        setBackground(Color.BLACK);

        // Play Again button
        JButton playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.setBounds(200, 550, 350, 100);
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 20));
        playAgainButton.setOpaque(true);
        playAgainButton.setContentAreaFilled(true);
        playAgainButton.setBackground(new Color(100, 150, 100));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setBorderPainted(true);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playAgainButton.addActionListener(playAgainAction);
        add(playAgainButton);

        // Main Menu button
        JButton mainMenuButton = new JButton("MAIN MENU");
        mainMenuButton.setBounds(730, 550, 350, 100);
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 20));
        mainMenuButton.setOpaque(true);
        mainMenuButton.setContentAreaFilled(true);
        mainMenuButton.setBackground(new Color(150, 100, 100));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setBorderPainted(true);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainMenuButton.addActionListener(mainMenuAction);
        add(mainMenuButton);

        // Score label
        scoreLabel = new JLabel("FINAL SCORE: 0");
        scoreLabel.setBounds(200, 250, 880, 100);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        add(scoreLabel);

        // Health label
        healthLabel = new JLabel("HEARTS REMAINING: 0");
        healthLabel.setBounds(200, 380, 880, 80);
        healthLabel.setFont(new Font("Arial", Font.BOLD, 36));
        healthLabel.setForeground(Color.RED);
        healthLabel.setHorizontalAlignment(JLabel.CENTER);
        add(healthLabel);
    }

    public void setFinalScore(long score) {
        this.finalScore = score;
        scoreLabel.setText("FINAL SCORE: " + score);
    }

    public void setRemainingHealth(int health) {
        this.remainingHealth = health;
        healthLabel.setText("HEARTS REMAINING: " + health);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw semi-transparent overlay for better text visibility
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 150, getWidth(), 500);
        
        // Draw "LEVEL COMPLETE" title
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 64));
        String title = "LEVEL COMPLETE!";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - titleWidth) / 2, 150);
    }
}
