package teamProject.model.tiles;

import java.io.*;
import java.util.*;
import teamProject.model.*;

public class TileFactory {
    // convert CSV to actual tiles
    public static TileMap fromCSV(int level) {
        List<List<String>> rows = new ArrayList<>();
        // create a new input stream for CSV file, bufferedReader to read the file
        InputStream is = TileFactory.class.getResourceAsStream("/levels/level" + level + ".csv");
        if (is == null) throw new RuntimeException("Level file not found: /levels/level" + level + ".csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while((line = br.readLine()) != null) {
                List<String> row = new ArrayList<>();
                for (String v : line.split(",")) {
                    row.add(v.trim()); // trim prevents whitespace
                }
                rows.add(row);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load level " + level, e);
        }
        return toTileGrid(rows);
    }

    public static TileMap toTileGrid(List<List<String>> rows) {
        int numRows = rows.size();
        int numCols = rows.get(0).size();
        TileMap map = new TileMap(numRows, numCols);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                String code = rows.get(r).get(c);
                Coordinate pos = new Coordinate(c, r);
                Tile tile;
                if (code.startsWith("L") && code.length() == 2) {
                    tile = new LetterTile(code.charAt(1));
                } else {
                    tile = switch (code) {
                        case "W"  -> new WallTile();
                        case "S"  -> new SpikeTile();
                        case "D"  -> new DestructibleTile();
                        case "ST" -> new StartTile();
                        case "EN" -> new EndTile();
                        case "ES" -> new EmptyTile();
                        default   -> new EmptyTile();
                    };
                }
                map.setTile(pos, tile);
                if (code.equals("ES")) {
                    map.addEnemySpawn(pos);
                }
            }
        }
        return map;
    }
}
