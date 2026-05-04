package teamProject.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveManager {
    private static final Logger LOGGER = Logger.getLogger(SaveManager.class.getName());
    private static final String SAVE_DIR  = System.getProperty("user.dir") + "/saves";
    private static final String SAVE_FILE = SAVE_DIR + "/save.json";

    private SaveManager() {}

    // --- Helpers ---

    private static String readFile() {
        try {
            return Files.readString(Path.of(SAVE_FILE));
        } catch (Exception e) {
            return "";
        }
    }

    private static void writeFile(int maxUnlockedLevel, long highScore) {
        try {
            Files.createDirectories(Path.of(SAVE_DIR));
            Files.writeString(Path.of(SAVE_FILE),
                "{\"maxUnlockedLevel\":" + maxUnlockedLevel + ",\"highScore\":" + highScore + "}");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Save failed", e);
        }
    }

    // --- Load ---

    public static int getMaxUnlockedLevel() {
        String content = readFile();
        int idx = content.indexOf("\"maxUnlockedLevel\":");
        if (idx < 0) return 1;
        try {
            String sub = content.substring(idx + 19).trim();
            return Integer.parseInt(sub.replaceAll("[^0-9].*", "").trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public static long getHighScore() {
        String content = readFile();
        int idx = content.indexOf("\"highScore\":");
        if (idx < 0) return 0;
        try {
            String sub = content.substring(idx + 12).trim();
            return Long.parseLong(sub.replaceAll("[^0-9].*", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }

    // --- Save ---

    public static void unlockLevel(int level) {
        if (level <= getMaxUnlockedLevel()) return;
        writeFile(level, getHighScore());
    }

    public static void saveHighScore(long score) {
        if (score <= getHighScore()) return;
        writeFile(getMaxUnlockedLevel(), score);
    }
}
