# The Blind Mice

A 2D tile-based platformer built in Java (Swing). You play as a mouse navigating a level, collecting letter tiles to spell words that grant temporary powers — then using those powers to outmaneuver obstacles and reach the exit.

---

## Team

- **Jack** — game logic, controller, power system
- **Lena** — art, sprites, UI design
- **Ali** — UI wiring, player/tile integration, scene assembly

---

## How to Run

```bash
./gradlew run
```

Requires Java 21+.

---

## Controls

| Key | Action |
|-----|--------|
| A | Move left |
| D | Move right |
| W | Jump |
| S | Break tile below (when GROW is active) |
| Escape | Pause |

---

## Gameplay

Each level is a grid of 40×26 tiles at 32 px each (1280×832 px). The mouse starts at the green **Start** tile and must reach the gold **End** tile to complete the level.

Letter tiles are scattered across the level. Walking through one adds it to your letter bank (shown in the HUD). When your collected letters can spell a power word, it activates automatically:

| Word | Effect |
|------|--------|
| JUMP | Higher jumps |
| LIFE | Restore one heart |
| SPEED | Faster movement |
| GROW | Break destructible walls |
| SMALL | Shrink hitbox |

You start with 3 hearts. Touching a spike or enemy costs one heart. Zero hearts = game over.

Score = 10 000 − (elapsed time in 100 ms units) + (hearts remaining × 1000).

Progress is saved automatically. Beating a level unlocks the next one in Level Select.

---

## Levels

| Level | Difficulty | Description |
|-------|-----------|-------------|
| 1 | Intro | Ground path with two spike obstacles; optional upper platforms hold **JUMP** and **LIFE** letters |
| 2 | Medium | Platform jumping required to reach the exit; **SPEED** and **JUMP** letter sets; mid-air spike traps |
| 3 | Hard | Destructible block barriers require **GROW** power; dense spike fields; 4 enemies |

---

## Tile Types

| CSV Code | Tile | Behavior |
|----------|------|----------|
| W | Wall | Solid, impassable |
| E | Empty | Open floor / air |
| D | Destructible | Solid; breakable with GROW active |
| S | Spike | Damages player on contact |
| ST | Start | Player spawn point |
| EN | End | Level exit |
| ES | Enemy Spawn | Spawns a snake enemy at this position |
| L[x] | Letter | Collected on contact (e.g., `LJ`, `LU`) |

---

## Project Structure

```
app/src/main/java/teamProject/
  controller/   GameController, GameLoop, EnemyController,
                GameEventListener (interface)
  model/        Game, PlayerEntity, EnemyEntity, Coordinate
    tiles/      Tile (interface), WallTile, EmptyTile, DestructibleTile,
                SpikeTile, LetterTile, StartTile, EndTile, TileMap,
                TileCoordinate, TileFactory
    strategies/ PowerStrategy (abstract), DefaultStrategy, SpeedStrategy,
                JumpStrategy, GrowStrategy, SmallStrategy
  view/         GameView, GamePanel, GamePanelBuilder,
                HudPanel, BaseMenuPanel, MenuPanel, WinPanel, LosePanel,
                PausePanel, LevelSelectPanel
  util/         AssetLoader, LevelLoader, GameConstants, SaveManager

app/src/main/resources/
  assets/
    entities/   mouseLeft.png, mouseRight.png, enemySnake.png
      gui/      redHeart.png, blackHeart.png
    panels/     level1Background.png, level2Background.png, level3Background.png
    tiles/      regularWall.png, destructibleWall.png, spike.png
                letters/ A.png … Z.png
  levels/       level1.csv, level2.csv, level3.csv
```

---

## Architecture / Design Patterns

| Pattern | Where Used |
|---------|-----------|
| **Builder** | `GamePanelBuilder` — fluent builder for constructing `GamePanel` with all sprites and references |
| **Factory Method** | `TileFactory.fromCSV()` — parses CSV into a `TileMap`; scene wiring is done directly in `GameView`'s constructor |
| **Strategy** | `PowerStrategy` interface + `GrowStrategy`, `SmallStrategy`, `SpeedStrategy`, `JumpStrategy`, `DefaultStrategy` — swappable player behavior without branching logic in the controller |
| **Observer / Listener** | `GameEventListener` interface — controller fires events (`onLevelComplete`, `onGameOver`, `onGamePaused`, `onScoreChanged`, etc.); `GameView` reacts without tight coupling |
| **MVC** | **Model**: `Game`, `PlayerEntity`, `EnemyEntity`, tile classes; **View**: `GameView` + all panel classes; **Controller**: `GameController`, `GameLoop`, `EnemyController` |

---

## Save System

Progress is stored in `<project-dir>/saves/save.json` as `{"maxUnlockedLevel": N, "highScore": H}`. `SaveManager` reads and writes this file using only the standard library. Beating level N unlocks level N+1 in the Level Select screen. The high score persists across sessions and is shown on the main menu.
