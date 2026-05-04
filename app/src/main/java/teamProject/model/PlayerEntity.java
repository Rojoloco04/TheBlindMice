package teamProject.model;

import teamProject.model.strategies.DefaultStrategy;
import teamProject.model.strategies.PowerStrategy;

public class PlayerEntity {
    // constants
    public static final int MAX_HEALTH = 3;
    public static final double DEFAULT_JUMP_HEIGHT = 4.0;
    public static final double DEFAULT_MOVE_SPEED = 4.0;
    public static final int DEFAULT_SIZE = 64; // in px

    // member variables
    private int health;
    private double jumpHeight;
    private double moveSpeed;
    private PowerStrategy currPower; // active powerup
    private int size;
    private Coordinate curPos;
    private boolean tileBreaker;

    // constructor
    public PlayerEntity() {
        health = MAX_HEALTH;
        jumpHeight = DEFAULT_JUMP_HEIGHT;
        moveSpeed = DEFAULT_MOVE_SPEED;
        size = DEFAULT_SIZE;
        curPos = new Coordinate(100, 300);
        setPower(new DefaultStrategy());
    }

    // getters and setters
    public int getHealth() {
        return health;
    }

    public void heal() {
        if (health < MAX_HEALTH) health++;
        else System.err.println("Cannot heal above max health");
    }

    public void damage() {
        if (health == 0) die();
        else if (health > 0) health--;
        else System.err.println("Cannot take damage below 0 health");
    }

    public void die() {
        // game end
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

    public PowerStrategy getPower() {
        return currPower;
    }

    public void setPower(PowerStrategy newPowerStrategy) {
        if (currPower != null) currPower.remove(this);
        currPower = newPowerStrategy;
        newPowerStrategy.apply(this);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int newSize) {
        size = newSize;
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
