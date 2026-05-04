package teamProject.controller;

import java.util.List;

public interface GameEventListener {
    void onRenderRequested();
    void onPlayerDirectionChanged(char direction);
    void onLetterBankChanged(List<Character> letters);
    void onLevelComplete(long score, int health);
    void onScoreChanged(long score);
    void onHealthChanged(int health);
}
