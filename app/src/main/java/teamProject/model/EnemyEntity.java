package teamProject.model;

public class EnemyEntity {
    public static final int SIZE = 32;

    private Coordinate curPos;
    private int direction = 1;

    public EnemyEntity(Coordinate startPos) {
        this.curPos = startPos;
    }

    public Coordinate getPos() {
        return curPos;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void onPlayerContact(PlayerEntity player) {
        player.damage();
    }
}
