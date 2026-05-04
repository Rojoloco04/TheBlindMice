package teamProject.controller;

import java.util.List;
import java.util.Set;

public interface GameEventListener {
    Set<Character> getHeldKeys();
    void onRenderRequested();
    void onPlayerDirectionChanged(char direction);
    void onLetterBankChanged(List<Character> letters);
    void onLevelComplete(long score, int health, long timeElapsedMs);
    void onGameOver(long score);
    void onGamePaused();
    void onGameResumed();
    void onScoreChanged(long score);
    void onHealthChanged(int health);
    void onPowerChanged(String name, int ticksRemaining);
    void onTimeChanged(long elapsedMs);
}
