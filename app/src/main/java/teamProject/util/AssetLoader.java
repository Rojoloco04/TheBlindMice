package teamProject.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AssetLoader {
    private static final Logger LOGGER = Logger.getLogger(AssetLoader.class.getName());
    private static final String TILES_PATH = "/assets/tiles/";
    private static final String ENTITIES_PATH = "/assets/entities/";
    private static final String BACKGROUNDS_PATH = "/assets/backgrounds/";
    private static final String GUI_PATH = "/assets/entities/gui/";
    private static final HashMap<String, BufferedImage> assets = new HashMap<>();

    private AssetLoader() {}

    // --- Private Helpers ---

    private static BufferedImage loadImage(String path) {
        try (InputStream is = AssetLoader.class.getResourceAsStream(path)) {
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

    private static ImageIcon loadScaledIcon(String path, int size) {
        BufferedImage img = loadImage(path);
        if (img == null) return null;
        Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // --- Panels & HUD ---

    public static BufferedImage loadBackground(int level) {
        return loadImage(BACKGROUNDS_PATH + "level" + level + "Background.png");
    }

    public static ImageIcon loadRedHeart(int size) {
        return loadScaledIcon(GUI_PATH + "redHeart.png", size);
    }

    public static ImageIcon loadBlackHeart(int size) {
        return loadScaledIcon(GUI_PATH + "blackHeart.png", size);
    }

    // --- Entities ---

    public static BufferedImage loadMouseLeft() {
        return loadImage(ENTITIES_PATH + "mouseLeft.png");
    }

    public static BufferedImage loadMouseRight() {
        return loadImage(ENTITIES_PATH + "mouseRight.png");
    }

    public static BufferedImage loadEnemySnake() {
        return loadImage(ENTITIES_PATH + "enemySnake.png");
    }

    // --- Tiles ---

    public static BufferedImage loadWall() {
        return loadImage(TILES_PATH + "regularWall.png");
    }

    public static BufferedImage loadDestructibleWall() {
        return loadImage(TILES_PATH + "destructibleWall.png");
    }

    public static BufferedImage loadSpike() {
        return loadImage(TILES_PATH + "spike.png");
    }

    public static void loadLetterAssets() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            char letter = alphabet.charAt(i);
            String path = "/assets/tiles/letters/" + letter + ".png";
            BufferedImage img = loadImage(path);
            if (img != null) {
                assets.put("LETTER_" + letter, img);
            }
        }
    }

    // --- Cache ---

    public static BufferedImage getAsset(String key) {
        return assets.get(key);
    }
}
