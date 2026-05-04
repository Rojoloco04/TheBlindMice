package teamProject.util;

import java.util.ArrayList;
import java.util.List;

import teamProject.model.Coordinate;
import teamProject.model.EnemyEntity;
import teamProject.model.Game;
import teamProject.model.tiles.TileFactory;
import teamProject.model.tiles.TileMap;

public class LevelLoader {
    public static void load(int level, Game game) {
        TileMap tiles = TileFactory.fromCSV(level);
        game.setTiles(tiles);
        game.setEnemies(loadEnemies(tiles));
    }

    private static List<EnemyEntity> loadEnemies(TileMap tiles) {
        List<EnemyEntity> enemies = new ArrayList<>();
        for (Coordinate spawn : tiles.getEnemySpawns()) {
            double px = spawn.getX() * GameConstants.TILE_SIZE;
            double py = spawn.getY() * GameConstants.TILE_SIZE;
            enemies.add(new EnemyEntity(new Coordinate(px, py)));
        }
        return enemies;
    }
}
