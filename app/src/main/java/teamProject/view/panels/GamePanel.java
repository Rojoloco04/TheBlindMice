package teamProject.view.panels;

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
import teamProject.model.tiles.LetterTile;
import teamProject.model.tiles.SpikeTile;
import teamProject.model.tiles.StartTile;
import teamProject.model.tiles.Tile;
import teamProject.model.tiles.TileMap;
import teamProject.model.tiles.WallTile;
import teamProject.util.AssetLoader;
import teamProject.util.GameConstants;

public class GamePanel extends JPanel {
    private static final int SPRITE_SIZE = 64;

    // model
    private TileMap tileMap;
    private final PlayerEntity player;
    private List<EnemyEntity> enemies;

    // ui
    private final HudPanel hudPanel;

    // sprites
    private BufferedImage backgroundImage;
    private final BufferedImage wallImage;
    private final BufferedImage destructibleImage;
    private final BufferedImage spikeImage;
    private final BufferedImage mouseLeft;
    private final BufferedImage mouseRight;
    private BufferedImage currentMouseSprite;
    private final BufferedImage enemySprite;

    // --- Lifecycle ---

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

    // --- Model ---

    public void setBackgroundImage(BufferedImage bg) {
        this.backgroundImage = bg;
    }

    public void updateRenderData(TileMap newTileMap, List<EnemyEntity> newEnemies) {
        this.tileMap = newTileMap;
        this.enemies = newEnemies;
    }

    public void setMouseDirection(char direction) {
        if (direction == 'A') {
            currentMouseSprite = mouseLeft;
        } else if (direction == 'D') {
            currentMouseSprite = mouseRight;
        }
        repaint();
    }

    // --- Rendering ---

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
                    } else if (tile instanceof DestructibleTile dt) {
                        if (!dt.isDestroyed()) {
                            img = destructibleImage;
                        }
                    } else if (tile instanceof SpikeTile) {
                        img = spikeImage;
                    } else if (tile instanceof StartTile) {
                        g2.setColor(new Color(50, 200, 80));
                        g2.fillRect(c * GameConstants.TILE_SIZE, r * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
                    } else if (tile instanceof EndTile) {
                        g2.setColor(new Color(220, 180, 40));
                        g2.fillRect(c * GameConstants.TILE_SIZE, r * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
                    } else if (tile instanceof LetterTile) {
                        LetterTile lt = (LetterTile) tile;
                        if (!lt.isCollected()) {
                            int x = c * GameConstants.TILE_SIZE;
                            int y = r * GameConstants.TILE_SIZE;
                            BufferedImage letterImg = AssetLoader.getAsset("LETTER_" + lt.getLetter());
                            if (letterImg != null) {
                                g2.drawImage(letterImg, x, y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                            } else {
                                g2.setColor(new Color(255, 230, 100));
                                g2.fillRect(x, y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
                                g2.setColor(Color.BLACK);
                                g2.drawString(String.valueOf(lt.getLetter()), x + GameConstants.TILE_SIZE / 4, y + GameConstants.TILE_SIZE * 3 / 4);
                            }
                        }
                    }
                    if (img != null) {
                        g2.drawImage(img, c * GameConstants.TILE_SIZE, r * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                    }
                }
            }
        }

        if (currentMouseSprite != null && player != null) {
            Coordinate pos = player.getPos();
            int drawSize = player.getWidth() == PlayerEntity.DEFAULT_WIDTH
                ? SPRITE_SIZE
                : SPRITE_SIZE * 3 / 2;
            int hitboxCenterX = (int) pos.getX() + PlayerEntity.HITBOX_OFFSET_X + player.getWidth() / 2;
            int hitboxBottom  = (int) pos.getY() + PlayerEntity.HITBOX_OFFSET_Y + player.getHeight();
            int footMargin    = SPRITE_SIZE - PlayerEntity.DEFAULT_HEIGHT - PlayerEntity.HITBOX_OFFSET_Y;
            int drawX = hitboxCenterX - drawSize / 2;
            int drawY = hitboxBottom  - drawSize + footMargin;
            g2.drawImage(currentMouseSprite, drawX, drawY, drawSize, drawSize, null);
        }

        if (enemySprite != null && enemies != null) {
            for (EnemyEntity enemy : enemies) {
                Coordinate pos = enemy.getPos();
                g2.drawImage(enemySprite, (int) pos.getX(), (int) pos.getY(), EnemyEntity.SIZE, EnemyEntity.SIZE, null);
            }
        }
    }
}
