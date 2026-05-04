package teamProject.controller;

import teamProject.view.GameView;

public class GameLoop {
    public enum GameStatus {
        RUNNING,
        STOPPED
    }

    private static final int DEFAULT_TPS = 60;

    private final GameController controller;
    private GameStatus status;
    private int tickRate;

    // --- Lifecycle ---

    public GameLoop(int level) {
        controller = new GameController(level);
        status = GameStatus.STOPPED;
        tickRate = DEFAULT_TPS;
    }

    void setTickRate(int tps) {
        tickRate = tps;
    }

    public void run() {
        controller.startGame();
        GameView view = new GameView(controller);
        controller.setListener(view);
        status = GameStatus.RUNNING;
    }

    void stop() {
        status = GameStatus.STOPPED;
    }

    public boolean isGameRunning() {
        return status == GameStatus.RUNNING;
    }

    // --- Loop ---

    public void processGameLoop() {
        long tickDuration = 1_000_000_000L / tickRate;
        long nextTick = System.nanoTime();
        while (isGameRunning()) {
            long now = System.nanoTime();
            if (now >= nextTick) {
                update();
                render();
                nextTick += tickDuration;
            } else {
                long sleepMs = (nextTick - now) / 1_000_000;
                if (sleepMs > 0) {
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    // --- Per-tick ---

    void update() {
        if (controller.isGameEnded() || controller.isPaused()) {
            return;
        }
        controller.timeUpdate();
        controller.scoreUpdate();
        controller.updateEnemies();
        controller.updatePlayer();
    }

    void render() {
        controller.updateScoreDisplay();
        controller.updateHealthDisplay();
        controller.updateTimeDisplay();
        controller.renderFrame();
    }
}
