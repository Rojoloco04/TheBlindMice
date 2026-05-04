package teamProject;

import teamProject.controller.GameLoop;

public class Main {
    public static void main(String[] args) {
        GameLoop threeBlindMice = new GameLoop(1);
        threeBlindMice.run();
        threeBlindMice.processGameLoop();
    }
}