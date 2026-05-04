package teamProject.model.tiles;

import java.util.ArrayList;
import java.util.List;
import teamProject.model.Coordinate;
import teamProject.util.GameConstants;

public class TileMap {

    private Tile[][] map;
    private Coordinate startPos;
    private Coordinate endPos;
    private List<Coordinate> enemySpawns = new ArrayList<>();

    public TileMap(int rows, int cols) {
        map = new Tile[rows][cols];
    }

    // takes tile-index coordinates for rendering
    public Tile getTile(Coordinate tilePos) {
        return map[(int)tilePos.getY()][(int)tilePos.getX()];
    }

    public void setTile(Coordinate pos, Tile type) {
        map[(int)pos.getY()][(int)pos.getX()] = type;
        if (type instanceof StartTile) {
            startPos = pos;
        }
        if (type instanceof EndTile) {
            endPos = pos;
        }
    }

    public Coordinate getStartPos() {
        return startPos;
    }

    public Coordinate getEndPos() {
        return endPos;
    }

    public int getRows() {
        return map.length;
    }

    public int getCols() {
        return map[0].length;
    }

    public void addEnemySpawn(Coordinate tilePos) {
        enemySpawns.add(tilePos);
    }

    public List<Coordinate> getEnemySpawns() {
        return enemySpawns;
    }

    // pixel coordinates to tile
    private TileCoordinate pixelToTile(Coordinate pixelPos) {
        int col = (int)(pixelPos.getX() / GameConstants.TILE_SIZE);
        int row = (int)(pixelPos.getY() / GameConstants.TILE_SIZE);
        return new TileCoordinate(row, col);
    }

    public Tile getTileAtPixel(Coordinate pixelPos) {
        TileCoordinate tc = pixelToTile(pixelPos);
        return map[tc.row][tc.col];
    }

    public boolean isSolid(Coordinate pixelPos) {
        TileCoordinate tc = pixelToTile(pixelPos);
        if (tc.row < 0 || tc.row >= map.length || tc.col < 0 || tc.col >= map[0].length) {
            return true;
        }
        return map[tc.row][tc.col].isSolid();
    }

    // Checks all four corners of a bounding box; all coordinates in pixels
    public boolean isBoundingBoxSolid(Coordinate topLeft, int width, int height) {
        double x = topLeft.getX();
        double y = topLeft.getY();
        return isSolid(topLeft)
            || isSolid(new Coordinate(x + width - 1, y))
            || isSolid(new Coordinate(x, y + height - 1))
            || isSolid(new Coordinate(x + width - 1, y + height - 1));
    }
}
