package teamProject.model;

import java.util.logging.Logger;

import teamProject.model.strategies.DefaultStrategy;
import teamProject.model.strategies.PowerStrategy;

public class PlayerEntity {
    private static final Logger LOGGER = Logger.getLogger(PlayerEntity.class.getName());
    public static final int MAX_HEALTH = 3;
    public static final double DEFAULT_JUMP_HEIGHT = 10.0;
    public static final double DEFAULT_MOVE_SPEED = 4.0;
    public static final int DEFAULT_WIDTH = 32;
    public static final int DEFAULT_HEIGHT = 48;
    public static final int HITBOX_OFFSET_X = 16; // centers hitbox over 64px sprite
    public static final int HITBOX_OFFSET_Y = 8;
    private static final long INVINCIBILITY_MS = 1000;

    private int health;
    private long lastDamageTime;
    private double jumpHeight;
    private double moveSpeed;
    private double vx;
    private double vy;
    private boolean grounded;
    private PowerStrategy currPower;
    private int width;
    private int height;
    private Coordinate curPos;
    private boolean tileBreaker;

    // --- Lifecycle ---

    public PlayerEntity() {
        reset();
    }

    public void reset() {
        health = MAX_HEALTH;
        jumpHeight = DEFAULT_JUMP_HEIGHT;
        moveSpeed = DEFAULT_MOVE_SPEED;
        vx = 0;
        vy = 0;
        grounded = false;
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        tileBreaker = false;
        lastDamageTime = 0;
        setPower(new DefaultStrategy());
    }

    // --- Health ---

    public int getHealth() {
        return health;
    }

    public void heal() {
        if (health < MAX_HEALTH) health++;
        else LOGGER.warning("Cannot heal above max health");
    }

    public void damage() {
        long now = System.currentTimeMillis();
        if (now - lastDamageTime < INVINCIBILITY_MS) {
            return;
        }
        lastDamageTime = now;
        if (health > 0) {
            health--;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    // --- Movement ---

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(double newJumpHeight) {
        jumpHeight = newJumpHeight;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(double newMoveSpeed) {
        moveSpeed = newMoveSpeed;
    }

    // --- Power ---

    public PowerStrategy getPower() {
        return currPower;
    }

    public void setPower(PowerStrategy newPowerStrategy) {
        if (currPower != null) currPower.remove(this);
        currPower = newPowerStrategy;
        newPowerStrategy.apply(this);
    }

    // --- Size & Position ---

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    public void setSize(int s) {
        width = s;
        height = s;
    }

    public void updatePos(Coordinate newPos) {
        curPos = newPos;
    }

    public Coordinate getPos() {
        return curPos;
    }

    public void setTileBreaker(boolean value) {
        tileBreaker = value;
    }

    public boolean canBreakTiles() {
        return tileBreaker;
    }
}
