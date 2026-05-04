package teamProject.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class PausePanel extends BaseMenuPanel {

    // --- Lifecycle ---

    public PausePanel(ActionListener resumeAction, ActionListener mainMenuAction) {
        setLayout(null);
        setBackground(new Color(20, 20, 20));

        JButton resumeButton = makeButton("RESUME", 250, 420, new Color(50, 100, 50), 340, 90, 22);
        resumeButton.addActionListener(resumeAction);
        add(resumeButton);

        JButton menuButton = makeButton("MAIN MENU", 690, 420, new Color(60, 60, 60), 340, 90, 22);
        menuButton.addActionListener(mainMenuAction);
        add(menuButton);
    }

    // --- Rendering ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 72));
        g.setColor(Color.WHITE);
        String title = "PAUSED";
        int w = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - w) / 2, 280);
    }
}
