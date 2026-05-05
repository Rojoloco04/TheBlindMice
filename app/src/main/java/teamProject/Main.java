package teamProject;

import teamProject.controller.GameLoop;

public class Main {
    public static void main(String[] args) {
        GameLoop theBlindMice = new GameLoop(1);
        theBlindMice.run();
        theBlindMice.processGameLoop();
    }
}