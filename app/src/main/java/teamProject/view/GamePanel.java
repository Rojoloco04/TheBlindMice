package teamProject.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import teamProject.model.Coordinate;
import teamProject.model.EnemyEntity;
import teamProject.model.PlayerEntity;
import teamProject.model.tiles.DestructibleTile;
import teamProject.model.tiles.EndTile;
import teamProject.model.tiles.SpikeTile;
import teamProject.model.tiles.StartTile;
import teamProject.model.tiles.Tile;
import teamProject.model.tiles.TileMap;
import teamProject.model.tiles.WallTile;
import teamProject.util.GameConstants;

public class GamePanel extends JPanel {

    // model
    private final TileMap tileMap;
    private final PlayerEntity player;
    private final List<EnemyEntity> enemies;

    // ui
    private final HudPanel hudPanel;

    // sprites
    private final BufferedImage backgroundImage;
    private final BufferedImage wallImage;
    private final BufferedImage destructibleImage;
    private final BufferedImage spikeImage;
    private final BufferedImage mouseLeft;
    private final BufferedImage mouseRight;
    private BufferedImage currentMouseSprite;
    private final BufferedImage enemySprite;

    // constructor using builder pattern
    GamePanel(GamePanelBuilder builder) {
        super(new BorderLayout());
        this.tileMap = builder.tileMap;
        this.player = builder.player;
        this.enemies = builder.enemies;
        this.hudPanel = builder.hudPanel;
        this.backgroundImage = builder.backgroundImage;
        this.wallImage = builder.wallImage;
        this.destructibleImage = builder.destructibleImage;
        this.spikeImage = builder.spikeImage;
        this.mouseLeft = builder.mouseLeft;
        this.mouseRight = builder.mouseRight;
        this.currentMouseSprite = builder.mouseRight;
        this.enemySprite = builder.enemySprite;

        JPanel leftAlignWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftAlignWrapper.setOpaque(false);
        leftAlignWrapper.add(hudPanel);
        add(leftAlignWrapper, BorderLayout.NORTH);

        setFocusable(true);
        addKeyListener(builder.keyListener);
    }

    @Override
    public Dimension getPreferredSize() {
        if (tileMap != null) {
            return new Dimension(tileMap.getCols() * GameConstants.TILE_SIZE, tileMap.getRows() * GameConstants.TILE_SIZE);
        }
        return super.getPreferredSize();
    }

    public void setMouseDirection(char direction) {
        if (direction == 'A') {
            currentMouseSprite = mouseLeft;
        } else if (direction == 'D') {
            currentMouseSprite = mouseRight;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        if (tileMap != null) {
            int rows = tileMap.getRows();
            int cols = tileMap.getCols();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    Tile tile = tileMap.getTile(new Coordinate(c, r));
                    BufferedImage img = null;
                    if (tile instanceof WallTile) {
                        img = wallImage;
                    } else if (tile instanceof DestructibleTile) {
                        img = destructibleImage;
                    } else if (tile instanceof SpikeTile) {
                        img = spikeImage;
                    } else if (tile instanceof StartTile) {
                        // TODO make start and end tile images
                        g2.setColor(new Color(50, 200, 80));
                        g2.fillRect(c * GameConstants.TILE_SIZE, r * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
                    } else if (tile instanceof EndTile) {
                        g2.setColor(new Color(220, 180, 40));
                        g2.fillRect(c * GameConstants.TILE_SIZE, r * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
                    }
                    if (img != null) {
                        g2.drawImage(img, c * GameConstants.TILE_SIZE, r * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                    }
                }
            }
        }

        if (currentMouseSprite != null && player != null) {
            Coordinate pos = player.getPos();
            g2.drawImage(currentMouseSprite, (int) pos.getX(), (int) pos.getY(), 64, 64, null);
        }

        if (enemySprite != null && enemies != null) {
            for (EnemyEntity enemy : enemies) {
                Coordinate pos = enemy.getPos();
                g2.drawImage(enemySprite, (int) pos.getX(), (int) pos.getY(), EnemyEntity.SIZE, EnemyEntity.SIZE, null);
            }
        }
    }
}
