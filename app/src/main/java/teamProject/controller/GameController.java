package teamProject.controller;

import java.util.ArrayList;
import java.util.List;

import teamProject.model.Coordinate;
import teamProject.model.EnemyEntity;
import teamProject.model.Game;
import teamProject.model.PlayerEntity;
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

public class GameController {
    private enum WordPower { GROW, SMALL, SPEED, JUMP, LIFE }

    private final PlayerEntity player;
    private GameEventListener listener;
    private final Game game;
    private final int level;
    private final EnemyController enemyController;

    public GameController(int level) {
        this.level = level;
        player = new PlayerEntity();
        game = new Game();
        enemyController = new EnemyController();
    }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public TileMap getTileMap() {
        return game.getTiles();
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

    // player movement
    public void tryPlayerMove(char direction) {
        Coordinate curPos = player.getPos();
        Coordinate newPos = new Coordinate(curPos.getX(), curPos.getY());
        listener.onPlayerDirectionChanged(direction);
        switch (direction) {
            case 'W' -> newPos.setY(curPos.getY() - player.getJumpHeight()); // up one
            case 'A' -> newPos.setX(curPos.getX() - player.getMoveSpeed()); // left one
            case 'S' -> newPos.setY(curPos.getY() + 1); // special case, when large can break Destructibles
            case 'D' -> newPos.setX(curPos.getX() + player.getMoveSpeed()); // right one
        }

        if (!testCollision(newPos)) {
            player.updatePos(newPos); // can move
            handleTileContact(game.getTiles().getTileAtPixel(newPos));
            listener.onRenderRequested();
        }
        else {
            System.err.println("Cannot move in that direction currently");
        }
    }

    private void handleTileContact(Tile tile) {
        if (tile instanceof LetterTile lt && !lt.isCollected()) {
            char letter = lt.getLetter();
            tile.onPlayerContact(player);
            game.addLetter(letter);
            checkWordCompletion();
            listener.onLetterBankChanged(game.getLetterBank());
        }
        else if (tile instanceof EndTile) {
            tile.onPlayerContact(player);
            // finalize score before showing win screen
            scoreUpdate();
            listener.onLevelComplete(game.getScore(), player.getHealth());
        }
        else {
            tile.onPlayerContact(player);
        }
    }

    private void checkWordCompletion() {
        for (WordPower wp : WordPower.values()) {
            if (canMake(wp.name(), game.getLetterBank())) {
                game.consumeWord(wp.name());
                switch (wp) {
                    case GROW  -> player.setPower(new GrowStrategy());
                    case SMALL -> player.setPower(new SmallStrategy());
                    case SPEED -> player.setPower(new SpeedStrategy());
                    case JUMP  -> player.setPower(new JumpStrategy());
                    case LIFE  -> player.heal();
                }
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

    public void processInput(char input) {
        switch (input) {
            case 'W', 'A', 'S', 'D' -> tryPlayerMove(input);
            case 'Q' -> System.out.println("Pause requested");
        }
    }

    public void timeUpdate() {
        long start = game.getStartTime();
        long curr = System.nanoTime();
        game.setTimeElapsed(curr - start);
    }

    public void scoreUpdate() {
        // score calculation logic here
        long score = Game.MAX_SCORE - (game.getTimeElapsed() / 100000000); // convert ns to seconds * 10
        score += player.getHealth() * Game.HEALTH_MULTIPLIER;
        if (score < 0) score = 0; // minimum score 0
        game.setScore(score);
    }

    public void updateScoreDisplay() {
        listener.onScoreChanged(game.getScore());
    }

    public void updateHealthDisplay() {
        listener.onHealthChanged(player.getHealth());
    }

    // collision test - enemies, letters, boundaries
    public boolean testCollision(Coordinate pos) {
        int spriteSize = player.getSize();
        if (game.getTiles().isBoundingBoxSolid(pos, spriteSize, spriteSize)) {
            return true;
        }
        for (EnemyEntity e : game.getEnemies()) {
            Coordinate ep = e.getPos();
            boolean overlapX = pos.getX() < ep.getX() + EnemyEntity.SIZE && pos.getX() + spriteSize > ep.getX();
            boolean overlapY = pos.getY() < ep.getY() + EnemyEntity.SIZE && pos.getY() + spriteSize > ep.getY();
            if (overlapX && overlapY) {
                e.onPlayerContact(player);
            }
        }
        return false;
    }

    public void updateEnemies() {
        enemyController.updateAll(game);
    }

    public List<EnemyEntity> getEnemies() {
        return game.getEnemies();
    }

    public int getLevel() {
        return level;
    }

    public void renderFrame() {
        listener.onRenderRequested();
    }
}
