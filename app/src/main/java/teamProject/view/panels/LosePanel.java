package teamProject.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class LosePanel extends BaseMenuPanel {
    private final JLabel scoreLabel;

    // --- Lifecycle ---

    public LosePanel(ActionListener retryAction, ActionListener mainMenuAction) {
        setLayout(null);
        setBackground(new Color(60, 10, 10));

        JButton retryButton = makeButton("RETRY", 250, 450, new Color(120, 60, 60), 340, 90, 22);
        retryButton.addActionListener(retryAction);
        add(retryButton);

        JButton menuButton = makeButton("MAIN MENU", 690, 450, new Color(60, 60, 60), 340, 90, 22);
        menuButton.addActionListener(mainMenuAction);
        add(menuButton);

        scoreLabel = new JLabel("SCORE: 0");
        scoreLabel.setBounds(150, 330, 980, 70);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        add(scoreLabel);
    }

    // --- Updates ---

    public void setScore(long score) {
        scoreLabel.setText("SCORE: " + score);
    }

    // --- Rendering ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(60, 10, 10));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 72));
        g.setColor(new Color(220, 60, 60));
        String title = "GAME OVER";
        int w = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - w) / 2, 280);
    }
}
