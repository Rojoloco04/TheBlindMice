package teamProject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import teamProject.model.Coordinate;
import teamProject.model.EnemyEntity;
import teamProject.model.Game;
import teamProject.model.PlayerEntity;
import teamProject.model.strategies.DefaultStrategy;
import teamProject.model.strategies.PowerStrategy;
import teamProject.model.strategies.GrowStrategy;
import teamProject.model.strategies.JumpStrategy;
import teamProject.model.strategies.SmallStrategy;
import teamProject.model.strategies.SpeedStrategy;
import teamProject.model.tiles.EndTile;
import teamProject.model.tiles.LetterTile;
import teamProject.model.tiles.Tile;
import teamProject.model.tiles.TileMap;
import teamProject.util.GameConstants;
import teamProject.util.LevelLoader;
import teamProject.util.SaveManager;

public class GameController {
    private enum WordPower { GROW, SMALL, SPEED, JUMP, LIFE }

    private static final int POWER_DURATION_TICKS = 300;
    private static final long NS_PER_DECISECOND = 100_000_000L; // nanoseconds per 0.1 second
    private static final long NS_PER_MS = 1_000_000L;

    private final PlayerEntity player;
    private GameEventListener listener;
    private final Game game;
    private int level;
    private final EnemyController enemyController;
    private boolean gameEnded;
    private boolean paused;
    private volatile boolean jumpRequested;
    private int powerExpiryTick;
    private PowerStrategy activeStrategy;

    // --- Lifecycle ---

    public GameController(int level) {
        this.level = level;
        player = new PlayerEntity();
        game = new Game();
        enemyController = new EnemyController();
    }

    public void startGame() {
        // load enemies and tiles before creating UI so the AWT thread never sees null tiles
        LevelLoader.load(level, game);
        // spawn player at the level's designated start position (tile -> pixel converted)
        Coordinate start = game.getTiles().getStartPos();
        if (start != null) {
            player.updatePos(new Coordinate(
                start.getX() * GameConstants.TILE_SIZE,
                (start.getY() - 1) * GameConstants.TILE_SIZE
            ));
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void reset() {
        gameEnded = false;
        paused = false;
        jumpRequested = false;
        powerExpiryTick = 0;
        activeStrategy = new DefaultStrategy();
        player.reset();
        game.reset();
        LevelLoader.load(level, game);
        Coordinate start = game.getTiles().getStartPos();
        if (start != null) {
            player.updatePos(new Coordinate(
                start.getX() * GameConstants.TILE_SIZE,
                (start.getY() - 1) * GameConstants.TILE_SIZE
            ));
        }
        listener.onHealthChanged(player.getHealth());
        listener.onScoreChanged(game.getScore());
        listener.onLetterBankChanged(game.getLetterBank());
        listener.onPowerChanged("", 0);
    }

    // --- Accessors ---

    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public TileMap getTileMap() {
        return game.getTiles();
    }

    public List<EnemyEntity> getEnemies() {
        return game.getEnemies();
    }

    public int getLevel() {
        return level;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    // --- Input ---

    public void processInput(char input) {
        switch (input) {
            case 'W' -> jumpRequested = true;
        }
    }

    // --- Update ---

    public void updatePlayer() {
        // power duration countdown
        if (powerExpiryTick > 0) {
            powerExpiryTick--;
            if (powerExpiryTick == 0) {
                player.setPower(new DefaultStrategy());
                activeStrategy = new DefaultStrategy();
            }
            listener.onPowerChanged(activeStrategy.getName(), powerExpiryTick);
        }

        // jump: one-shot flag set by processInput on W keypress, consumed here
        if (jumpRequested && player.isGrounded()) {
            player.setVy(-player.getJumpHeight());
            player.setGrounded(false);
        }
        jumpRequested = false;

        // gravity
        player.setVy(Math.min(player.getVy() + GameConstants.GRAVITY, GameConstants.MAX_FALL_SPEED));

        // horizontal input from held keys
        Set<Character> held = listener.getHeldKeys();
        boolean left = held.contains('A');
        boolean right = held.contains('D');
        if (left && !right) {
            player.setVx(Math.max(player.getVx() - GameConstants.HORIZONTAL_ACCEL, -player.getMoveSpeed()));
        } else if (right && !left) {
            player.setVx(Math.min(player.getVx() + GameConstants.HORIZONTAL_ACCEL, player.getMoveSpeed()));
        } else {
            player.setVx(player.getVx() * GameConstants.HORIZONTAL_FRICTION);
            if (Math.abs(player.getVx()) < 0.1) {
                player.setVx(0);
            }
        }

        Coordinate cur = player.getPos();
        double newX = cur.getX();
        double newY = cur.getY();

        // resolve x and y separately so the player slides along walls
        Coordinate testX = new Coordinate(cur.getX() + player.getVx(), cur.getY());
        if (!collidesWithTile(testX)) {
            newX = cur.getX() + player.getVx();
        } else {
            player.setVx(0);
        }

        Coordinate testY = new Coordinate(newX, cur.getY() + player.getVy());
        if (!collidesWithTile(testY)) {
            newY = cur.getY() + player.getVy();
            player.setGrounded(false);
        } else {
            if (player.getVy() > 0) {
                player.setGrounded(true);
            }
            player.setVy(0);
        }

        player.updatePos(new Coordinate(newX, newY));

        // S held: check contacts 1px below to trigger breakable tile destruction (GROW state)
        if (held.contains('S')) {
            processTileContacts(new Coordinate(newX, newY + 1));
        }

        // update sprite direction based on actual movement
        if (player.getVx() < 0) listener.onPlayerDirectionChanged('A');
        else if (player.getVx() > 0) listener.onPlayerDirectionChanged('D');

        processEnemyContacts(new Coordinate(newX, newY));
        processTileContacts(player.getPos());

        if (!gameEnded && player.isDead()) {
            gameEnded = true;
            SaveManager.saveHighScore(game.getScore());
            listener.onGameOver(game.getScore());
        }
    }

    public void updateEnemies() {
        enemyController.updateAll(game);
    }

    public void renderFrame() {
        listener.onRenderRequested();
    }

    // --- Collision ---

    private boolean collidesWithTile(Coordinate pos) {
        Coordinate hitbox = new Coordinate(
            pos.getX() + PlayerEntity.HITBOX_OFFSET_X,
            pos.getY() + PlayerEntity.HITBOX_OFFSET_Y
        );
        return game.getTiles().isBlocked(hitbox, player.getWidth(), player.getHeight());
    }

    private void processTileContacts(Coordinate spritePos) {
        Coordinate hitbox = new Coordinate(
            spritePos.getX() + PlayerEntity.HITBOX_OFFSET_X,
            spritePos.getY() + PlayerEntity.HITBOX_OFFSET_Y
        );
        for (Tile t : game.getTiles().getOverlappingTiles(hitbox, player.getWidth(), player.getHeight())) {
            applyTileEffect(t);
        }
    }

    private void applyTileEffect(Tile tile) {
        if (tile instanceof LetterTile lt && !lt.isCollected()) {
            char letter = lt.getLetter();
            tile.onPlayerContact(player);
            game.addLetter(letter);
            checkWordCompletion();
            listener.onLetterBankChanged(game.getLetterBank());
        }
        else if (tile instanceof EndTile && !gameEnded) {
            gameEnded = true;
            tile.onPlayerContact(player);
            scoreUpdate();
            SaveManager.unlockLevel(level + 1);
            SaveManager.saveHighScore(game.getScore());
            listener.onLevelComplete(game.getScore(), player.getHealth(), game.getTimeElapsed() / NS_PER_MS);
        }
        else {
            tile.onPlayerContact(player);
        }
    }

    private void processEnemyContacts(Coordinate pos) {
        int pw = player.getWidth();
        int ph = player.getHeight();
        for (EnemyEntity e : game.getEnemies()) {
            Coordinate ep = e.getPos();
            boolean overlapX = pos.getX() < ep.getX() + EnemyEntity.SIZE && pos.getX() + pw > ep.getX();
            boolean overlapY = pos.getY() < ep.getY() + EnemyEntity.SIZE && pos.getY() + ph > ep.getY();
            if (overlapX && overlapY) {
                e.onPlayerContact(player);
            }
        }
    }

    // --- Word / Power ---

    private void checkWordCompletion() {
        for (WordPower wp : WordPower.values()) {
            if (!canMake(wp.name(), game.getLetterBank())) continue;
            game.consumeWord(wp.name());
            if (wp == WordPower.LIFE) {
                player.heal();
            } else {
                switch (wp) {
                    case GROW  -> activeStrategy = new GrowStrategy();
                    case SMALL -> activeStrategy = new SmallStrategy();
                    case SPEED -> activeStrategy = new SpeedStrategy();
                    case JUMP  -> activeStrategy = new JumpStrategy();
                    default    -> {}
                }
                player.setPower(activeStrategy);
                powerExpiryTick = POWER_DURATION_TICKS;
                listener.onPowerChanged(activeStrategy.getName(), powerExpiryTick);
            }
        }
    }

    private boolean canMake(String word, List<Character> bank) {
        List<Character> remaining = new ArrayList<>(bank);
        for (char c : word.toCharArray()) {
            if (!remaining.remove((Character) c)) return false;
        }
        return true;
    }

    // --- Scoring ---

    public void timeUpdate() {
        long start = game.getStartTime();
        long curr = System.nanoTime();
        game.setTimeElapsed(curr - start);
    }

    public void scoreUpdate() {
        long score = Game.MAX_SCORE - (game.getTimeElapsed() / NS_PER_DECISECOND);
        score += player.getHealth() * Game.HEALTH_MULTIPLIER;
        if (score < 0) score = 0;
        game.setScore(score);
    }

    public void updateScoreDisplay() {
        listener.onScoreChanged(game.getScore());
    }

    public void updateHealthDisplay() {
        listener.onHealthChanged(player.getHealth());
    }

    public void updateTimeDisplay() {
        listener.onTimeChanged(game.getTimeElapsed() / NS_PER_MS);
    }
}
