package teamProject.model.tiles;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import teamProject.model.Coordinate;
import teamProject.util.GameConstants;

public class TileMap {

    private Tile[][] map;
    private Coordinate startPos;
    private Coordinate endPos;
    private List<Coordinate> enemySpawns = new ArrayList<>();

    // --- Lifecycle ---

    public TileMap(int rows, int cols) {
        map = new Tile[rows][cols];
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

    // --- Accessors ---

    public Tile getTile(Coordinate tilePos) {
        return map[(int)tilePos.getY()][(int)tilePos.getX()];
    }

    public Tile getTileAtPixel(Coordinate pixelPos) {
        TileCoordinate tc = pixelToTile(pixelPos);
        return map[tc.row][tc.col];
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

    // --- Enemy Spawns ---

    public void addEnemySpawn(Coordinate tilePos) {
        enemySpawns.add(tilePos);
    }

    public List<Coordinate> getEnemySpawns() {
        return enemySpawns;
    }

    // --- Collision ---

    public boolean isSolid(Coordinate pixelPos) {
        TileCoordinate tc = pixelToTile(pixelPos);
        if (tc.row < 0 || tc.row >= map.length || tc.col < 0 || tc.col >= map[0].length) {
            return true;
        }
        return map[tc.row][tc.col].isSolid();
    }

    // Returns all unique tiles overlapping the given pixel bounding box. Assumes in-bounds.
    public Set<Tile> getOverlappingTiles(Coordinate topLeft, int width, int height) {
        Set<Tile> tiles = new LinkedHashSet<>();
        int left   = (int)(topLeft.getX() / GameConstants.TILE_SIZE);
        int right  = (int)((topLeft.getX() + width  - 1) / GameConstants.TILE_SIZE);
        int top    = (int)(topLeft.getY() / GameConstants.TILE_SIZE);
        int bottom = (int)((topLeft.getY() + height - 1) / GameConstants.TILE_SIZE);
        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                tiles.add(map[row][col]);
            }
        }
        return tiles;
    }

    // Returns true if the bounding box touches a solid tile or the map boundary; all coordinates in pixels
    public boolean isBlocked(Coordinate topLeft, int width, int height) {
        int left   = (int)(topLeft.getX() / GameConstants.TILE_SIZE);
        int right  = (int)((topLeft.getX() + width  - 1) / GameConstants.TILE_SIZE);
        int top    = (int)(topLeft.getY() / GameConstants.TILE_SIZE);
        int bottom = (int)((topLeft.getY() + height - 1) / GameConstants.TILE_SIZE);
        if (top < 0 || bottom >= map.length || left < 0 || right >= map[0].length) {
            return true;
        }
        return getOverlappingTiles(topLeft, width, height).stream().anyMatch(Tile::isSolid);
    }

    // --- Private ---

    private TileCoordinate pixelToTile(Coordinate pixelPos) {
        int col = (int)(pixelPos.getX() / GameConstants.TILE_SIZE);
        int row = (int)(pixelPos.getY() / GameConstants.TILE_SIZE);
        return new TileCoordinate(row, col);
    }
}
