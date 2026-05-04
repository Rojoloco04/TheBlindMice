package teamProject.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HudPanel extends JPanel {
    private final JLabel scoreLabel;
    private final JLabel letterBankLabel;
    private final JLabel[] heartLabels = new JLabel[3];
    private final ImageIcon redHeartIcon;
    private final ImageIcon blackHeartIcon;

    public HudPanel(ImageIcon redHeartIcon, ImageIcon blackHeartIcon) {
        this.redHeartIcon = redHeartIcon;
        this.blackHeartIcon = blackHeartIcon;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));

        JPanel heartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        heartPanel.setOpaque(false);
        heartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 3; i++) {
            heartLabels[i] = new JLabel(redHeartIcon);
            heartPanel.add(heartLabels[i]);
            if (i < 2) heartPanel.add(Box.createRigidArea(new Dimension(3, 0)));
        }

        scoreLabel = new JLabel("SCORE: 0");
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        letterBankLabel = new JLabel("LETTERS: ");
        letterBankLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        letterBankLabel.setForeground(Color.WHITE);
        letterBankLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(heartPanel);
        add(Box.createRigidArea(new Dimension(0, 2)));
        add(scoreLabel);
        add(Box.createRigidArea(new Dimension(0, 2)));
        add(letterBankLabel);
    }

    public void updateScore(long score) {
        scoreLabel.setText("SCORE: " + score);
    }

    public void updateHealth(int health) {
        for (int i = 0; i < 3; i++) {
            heartLabels[i].setIcon(health > i ? redHeartIcon : blackHeartIcon);
        }
    }

    public void updateLetterBank(List<Character> letters) {
        StringBuilder sb = new StringBuilder("LETTERS: ");
        for (char c : letters) {
            sb.append(c).append(' ');
        }
        letterBankLabel.setText(sb.toString().trim());
    }
}
