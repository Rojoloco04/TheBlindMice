package teamProject.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AssetLoader {
    private static final Logger LOGGER = Logger.getLogger(AssetLoader.class.getName());
    private static final String TILES_PATH = "/assets/tiles/";
    private static final String ENTITIES_PATH = "/assets/entities/";
    private static final String PANELS_PATH = "/assets/panels/";

    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                LOGGER.log(Level.SEVERE, "Resource not found: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading image: " + path, e);
            return null;
        }
    }

    private ImageIcon loadScaledIcon(String path, int size) {
        BufferedImage img = loadImage(path);
        if (img == null) return null;
        Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public ImageIcon loadRedHeart(int size) {
        return loadScaledIcon(PANELS_PATH + "redHeart.png", size);
    }

    public ImageIcon loadBlackHeart(int size) {
        return loadScaledIcon(PANELS_PATH + "blackHeart.png", size);
    }

    public BufferedImage loadMenuPanel() {
        return loadImage(PANELS_PATH + "menuPanel.png");
    }

    public BufferedImage loadMouseLeft() {
        return loadImage(ENTITIES_PATH + "mouseLeft.png");
    }

    public BufferedImage loadMouseRight() {
        return loadImage(ENTITIES_PATH + "mouseRight.png");
    }

    public BufferedImage loadBackground(int level) {
        return loadImage(PANELS_PATH + "level" + level + "Background.png");
    }

    public BufferedImage loadWall() {
        return loadImage(TILES_PATH + "regularWall.png");
    }

    public BufferedImage loadDestructibleWall() {
        return loadImage(TILES_PATH + "destructibleWall.png");
    }

    public BufferedImage loadSpike() {
        return loadImage(TILES_PATH + "spike.png");
    }

    public BufferedImage loadEnemySnake() {
        return loadImage(ENTITIES_PATH + "enemySnake.png");
    }
}