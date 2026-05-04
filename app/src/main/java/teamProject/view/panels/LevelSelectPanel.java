package teamProject.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.function.IntConsumer;

import javax.swing.JButton;

public class LevelSelectPanel extends BaseMenuPanel {
    private final JButton[] levelButtons = new JButton[3];

    // --- Lifecycle ---

    public LevelSelectPanel(IntConsumer onLevelSelected, ActionListener backAction) {
        setLayout(null);
        setBackground(new Color(20, 20, 50));

        for (int i = 0; i < 3; i++) {
            final int lvl = i + 1;
            JButton btn = makeButton("LEVEL " + lvl, 130 + i * 340, 360, new Color(50, 80, 150), 260, 100, 22);
            btn.addActionListener(e -> onLevelSelected.accept(lvl));
            levelButtons[i] = btn;
            add(btn);
        }

        JButton backButton = makeButton("BACK", 540, 540, new Color(60, 60, 60), 200, 70, 18);
        backButton.addActionListener(backAction);
        add(backButton);
    }

    // --- Refresh ---

    public void refresh(int maxUnlocked) {
        for (int i = 0; i < 3; i++) {
            boolean unlocked = (i + 1) <= maxUnlocked;
            levelButtons[i].setEnabled(unlocked);
            levelButtons[i].setBackground(unlocked ? new Color(50, 80, 150) : new Color(40, 40, 40));
        }
    }

    // --- Rendering ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(20, 20, 50));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 54));
        g.setColor(Color.WHITE);
        String title = "SELECT LEVEL";
        int w = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - w) / 2, 220);
    }
}
