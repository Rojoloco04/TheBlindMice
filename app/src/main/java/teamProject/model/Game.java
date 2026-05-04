package teamProject.model;

import java.util.ArrayList;
import java.util.List;

import teamProject.model.tiles.TileMap;

public class Game {
    public static final long MAX_SCORE = 10000;
    public static final long HEALTH_MULTIPLIER = 1000;

    private long startTime;
    private long timeElapsed;
    private long score;
    private List<Character> letterBank;
    private List<EnemyEntity> enemies;
    private TileMap tiles;

    // --- Lifecycle ---

    public Game() {
        startTime = System.nanoTime();
        timeElapsed = 0;
        score = 0;
        letterBank = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    public void reset() {
        startTime = System.nanoTime();
        timeElapsed = 0;
        score = 0;
        letterBank.clear();
        enemies.clear();
    }

    // --- Letter Bank ---

    public void addLetter(char c) {
        letterBank.add(c);
    }

    public List<Character> getLetterBank() {
        return letterBank;
    }

    public void consumeWord(String word) {
        for (char c : word.toCharArray()) {
            letterBank.remove(Character.valueOf(c));
        }
    }

    // --- Timing & Scoring ---

    public long getStartTime() {
        return startTime;
    }

    public void setTimeElapsed(long newTime) {
        timeElapsed = newTime;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long newScore) {
        score = newScore;
    }

    // --- Tiles & Enemies ---

    public TileMap getTiles() {
        return tiles;
    }

    public void setTiles(TileMap tiles) {
        this.tiles = tiles;
    }

    public List<EnemyEntity> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<EnemyEntity> enemies) {
        this.enemies = enemies;
    }
}
