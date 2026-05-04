package teamProject.util;

public class GameConstants {
    public static final int TILE_SIZE = 32;

    public static final double GRAVITY = 0.4;           // px/tick²
    public static final double MAX_FALL_SPEED = 12.0;   // px/tick terminal velocity
    public static final double HORIZONTAL_ACCEL = 0.8;  // px/tick² while key held
    public static final double HORIZONTAL_FRICTION = 0.8; // vx multiplier per tick when no input
}
