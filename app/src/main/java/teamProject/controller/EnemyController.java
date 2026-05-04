package teamProject.controller;

import teamProject.model.Coordinate;
import teamProject.model.EnemyEntity;
import teamProject.model.Game;
import teamProject.model.tiles.TileMap;

public class EnemyController {
    private static final int PATROL_SPEED = 1;

    public void updateAll(Game game) {
        for (EnemyEntity enemy : game.getEnemies()) {
            update(enemy, game.getTiles());
        }
    }

    private void update(EnemyEntity enemy, TileMap tileMap) {
        int dir = enemy.getDirection();
        double nextX = enemy.getPos().getX() + dir * PATROL_SPEED;
        double y = enemy.getPos().getY();

        double wallCheckX = dir > 0 ? nextX + EnemyEntity.SIZE : nextX - 1; // leading horizontal edge in movement direction
        double edgeCheckX = dir > 0 ? nextX + EnemyEntity.SIZE - 1 : nextX; // bottom corner of leading edge (1px inset to stay on tile)
        double edgeCheckY = y + EnemyEntity.SIZE; // one row below enemy's feet

        boolean wallAhead = tileMap.isSolid(new Coordinate(wallCheckX, y)); // solid tile blocking forward movement
        boolean edgeAhead = !tileMap.isSolid(new Coordinate(edgeCheckX, edgeCheckY)); // no floor tile ahead (would walk off ledge)

        if (wallAhead || edgeAhead) {
            enemy.setDirection(dir * -1);
        } else {
            enemy.getPos().setX(nextX);
        }
    }
}
