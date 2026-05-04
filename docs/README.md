# The Blind Mice

A 2D tile-based maze game built in Java (Swing). You play as a mouse navigating a level, collecting letter tiles to spell words that grant temporary powers — then using those powers to outmaneuver obstacles and reach the exit.

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
| W | Move up |
| A | Move left |
| D | Move right |
| S | Move down |
| Q | Pause |

---

## Gameplay

Each level is a grid of 40×25 tiles at 32 px each (1280×800 px). The mouse starts at the green **Start** tile and must reach the gold **End** tile to complete the level.

Letter tiles are scattered across the level. Walking through one adds it to your letter bank (shown in the HUD). When your collected letters can spell a power word, it activates automatically:

| Word | Effect |
|------|--------|
| SPEED | Faster movement |
| JUMP | Higher jumps |
| GROW | Expand hitbox — break destructible walls |
| SMALL | Shrink hitbox |
| LIFE | Restore one heart |

You start with 3 hearts. Touching a spike or enemy costs one heart. Zero hearts ends the run.

Score = 10 000 − (elapsed time in 100 ms units) + (hearts remaining × 1000).

---

## Tile Types

| CSV Code | Tile | Behavior |
|----------|------|----------|
| W | Wall | Solid, impassable |
| E | Empty | Open floor/air |
| D | Destructible | Solid; breakable with GROW active |
| S | Spike | Damages player on contact |
| ST | Start | Player spawn point |
| EN | End | Level exit |
| ES | Enemy Spawn | Spawns a snake enemy at this position |
| L[x] | Letter | Collected on contact (e.g., `LA`, `LG`) |

---

## Project Structure

```
app/src/main/java/teamProject/
  controller/   GameController, GameLoop, EnemyController,
                GameEventListener (interface)
  model/        Game, PlayerEntity, EnemyEntity, Coordinate
    tiles/      Tile (interface), WallTile, EmptyTile, DestructibleTile,
                SpikeTile, LetterTile, StartTile, EndTile, TileMap, TileFactory
    strategies/ PowerStrategy (abstract), DefaultStrategy, SpeedStrategy,
                JumpStrategy, GrowStrategy, SmallStrategy
  view/         GameView, GamePanel, GamePanelBuilder, GameSceneFactory,
                HudPanel, MenuPanel, WinPanel
  util/         AssetLoader, LevelLoader, GameConstants

app/src/main/resources/
  assets/
    entities/   mouseLeft.png, mouseRight.png, enemySnake.png
    panels/     menuPanel.png, level1Background.png, level2Background.png,
                redHeart.png, blackHeart.png
    tiles/      regularWall.png, destructibleWall.png, spike.png
                letters/ A.png … Z.png
  levels/       level1.csv
```

---

## Architecture

MVC pattern:

- **Model** — game state (`Game`, `PlayerEntity`, `TileMap`, letter bank, score)
- **View** — Swing panels; reads model state at paint time; `GameSceneFactory` assembles the full scene; `GamePanelBuilder` constructs `GamePanel`
- **Controller** — handles keyboard input, updates model, triggers repaints via `GameEventListener`

Power-up behavior uses the **Strategy** pattern: `PlayerEntity` holds an active `PowerStrategy`; swapping the strategy changes movement/size/ability without branching logic in the controller.

Model-view decoupling uses the **Observer** pattern: `GameEventListener` defines callbacks (`onRenderRequested`, `onPlayerDirectionChanged`, `onLetterBankChanged`, `onLevelComplete`, `onScoreChanged`, `onHealthChanged`) that `GameView` implements.

Levels are defined in CSV files and parsed by `TileFactory`, which maps cell codes to concrete `Tile` instances.

---

## Current Status

Working:
- WASD movement with solid-tile collision (4-corner bounding-box check)
- Letter collection and automatic word matching
- All five power-ups (SPEED, JUMP, GROW, SMALL, LIFE)
- Destructible wall breaking when GROW is active
- Spike damage and 3-heart health system
- Enemy snakes with patrol AI (reverse on walls and ledge edges)
- HUD (hearts, score, letter bank)
- Menu screen with start button
- Win screen with final score
- CSV-based level loading (level 1 complete)

Not yet implemented:
- Physics (gravity, momentum)
- Save / load system
- Additional levels (level 2 and 3)
