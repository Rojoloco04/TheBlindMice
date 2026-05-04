package teamProject.controller;

import teamProject.view.GameView;

public class GameLoop {
    // enum for game state
    public enum GameStatus {
        RUNNING,
        STOPPED
    }

    private static final int DEFAULT_TPS = 60;

    private final GameController controller;
    private GameStatus status;
    private int tickRate;

    public GameLoop(int level) {
        controller = new GameController(level);
        status = GameStatus.STOPPED;
        tickRate = DEFAULT_TPS;
    }

    public void setTickRate(int tps) {
        tickRate = tps;
    }

    public void run() {
        controller.startGame();
        GameView view = new GameView(controller);
        controller.setListener(view);
        status = GameStatus.RUNNING;
    }

    public void stop() {
        status = GameStatus.STOPPED;
    }

    public boolean isGameRunning() {
        return status == GameStatus.RUNNING;
    }

    public void update() {
        // update logic (time/score)
        controller.timeUpdate();
        controller.scoreUpdate();
        controller.updateEnemies();
    }

    public void render() {
        // render game state (using UI)
        controller.updateScoreDisplay();
        controller.updateHealthDisplay();
        controller.renderFrame();
    }

    public void processInput(char input) {
        switch (input) {
            case 'W', 'A', 'S', 'D' -> controller.tryPlayerMove(input);
            case 'Q' -> stop(); // pause
        }
    }

    // constant updates to game state
    // i.e. time/score calculations/enemy movement
    public void processGameLoop() {
        // logic for updating game state here
        long tickDuration = 1_000_000_000L / tickRate;
        long nextTick = System.nanoTime();
        while (isGameRunning()) {
            long now = System.nanoTime();
            if (now >= nextTick) {
                update();
                render();
                nextTick += tickDuration;
            }
            else {
                long sleepMs = (nextTick - now) / 1_000_000;
                if (sleepMs > 0) {
                    try {
                        Thread.sleep(sleepMs);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }
}
