package teamProject.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
    private final BufferedImage menuImage;

    public MenuPanel(BufferedImage menuImage, ActionListener startAction) {
        this.menuImage = menuImage;
        setLayout(null);

        JButton startButton = new JButton();
        startButton.setBounds(10, 550, 1500, 150);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(startAction);
        add(startButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.drawImage(menuImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("START SCREEN NOT FOUND IN RESOURCES", 180, 200);
        }
    }
}
