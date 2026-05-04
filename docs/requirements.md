# The Blind Mice
## Software Requirements Document
**Team: The Blind Mice**

---

## 1. Overview

*The Blind Mice* is a 2D side-scrolling platformer built using Java, particularly the Swing GUI framework. The player controls a mouse navigating a New York apartment complex, trying to escape from the top floors down to the street. The apartment's residents are actively trying to remove the mouse, so each floor presents a new set of obstacles and enemies to overcome.

The core mechanic is a letter-collection and word-spelling system. As the mouse moves through each level, it can pick up letter tiles scattered around the stage. Collected letters appear in a display at the top of the screen. When the player spells a valid word from the available letter bank, the mouse gains a temporary power corresponding to that word. For example, spelling "SPEED" increases the mouse's movement speed, while "JUMP" increases its jump height. These powers are the primary tool for clearing obstacles that would otherwise be impassable.

The mouse starts each run with 3 hearts. Taking damage from an obstacle or enemy costs one heart; reaching zero ends the game. Certain words can restore hearts. The game contains 3 levels, each representing a different floor of the apartment. The player progresses downward through them in sequence. Scoring is based on how quickly each level is completed, with bonus points awarded for hearts remaining when a level is cleared.

The game saves progress between sessions, including which levels have been completed and the player's high scores.

---

## 2. Movement and Physics Assumptions

The following assumptions govern how the mouse moves and how the platformer physics work:

- The mouse moves left and right along a 2D plane using keyboard input (arrow keys or WASD). There is no depth or Z-axis movement.
- The mouse can jump by pressing a designated key. Jump height is fixed unless modified by an active word power. The mouse cannot jump while already airborne (no double jump by default).
- Gravity is applied continuously. When the mouse walks off a platform edge, it falls downward at a constant acceleration until it lands on a surface or the ground.
- The mouse can only move horizontally; it cannot crawl, slide, or crouch unless a word power explicitly enables it.
- Collision with the floor, platforms, and walls is solid. The mouse cannot pass through these surfaces under normal conditions (invulnerability word power is an exception for obstacles, not for geometry).
- Enemies move along fixed patrol paths or react to the mouse entering their detection radius. They do not jump or navigate complex terrain.
- Letter tiles are placed at fixed positions in the level. The mouse collects a letter by walking through (overlapping) its tile.
- Word powers are activated manually by the player selecting a word from the collected letters, not automatically upon spelling a word.
- Active word powers are temporary. Each power lasts for a fixed duration, after which the mouse returns to its default state.
- Only one word power can be active at a time. Activating a new power replaces the current one.

---

## 3. Functional Requirements

### FR1: Letter Collection
The game tracks which letter tiles the player has collected in the current level. Collected letters are added to a pool displayed on the HUD. Letters remain in the pool until used to spell a word or the level ends. The player may hold multiple copies of the same letter if multiple tiles for that letter exist in the level.

### FR2: Word-Power System
The game maintains a fixed word bank of valid power words. The player can open a word-input interface during gameplay to attempt to spell a word using letters currently in their pool. If the spelled word is in the word bank and all required letters are available, the corresponding power is activated and the used letters are consumed from the pool. Invalid words or insufficient letters are rejected with a visual indicator. The available power words and their effects are:

| Word | Effect |
|------|--------|
| `SPEED` | Increased movement speed |
| `JUMP` | Increased jump height |
| `LIFE` | Restore one heart |
| `SMALL` | Shrink the mouse's hitbox |
| `GROW` | Expand the mouse's hitbox to push through certain obstacles |

### FR3: Health System
The mouse has a maximum of 3 hearts. Contact with a damaging obstacle or enemy reduces the heart count by one. When hearts reach zero the current run ends and the player is returned to the level select screen. The `LIFE` word power restores one heart, up to the maximum of 3. Hearts remaining at the end of a level contribute bonus points to the score.

### FR4: Collision Detection
The game detects and resolves collisions between the mouse and all solid geometry (floors, walls, platforms, ceilings), collectible letter tiles, damaging obstacles, and enemies. Solid geometry collisions prevent overlap and stop relevant velocity. Tile collisions trigger collection. Obstacle and enemy collisions trigger health reduction unless the `LIFE` power is active.

### FR5: Enemy AI
Enemies patrol fixed paths within a level. When the mouse enters a defined detection radius around an enemy, the enemy moves toward the mouse's position. Enemies deal one heart of damage on contact. Enemies do not jump or leave the ground. Enemies reset to their patrol path if the mouse moves outside their detection radius.

### FR6: Level Structure
The game contains 3 levels. Each level represents a different floor of the apartment complex, and the player progresses downward through them in a fixed sequence. Completing a level transitions the player automatically to the next one. Completing all 3 levels ends the game and displays the final score screen. Each level introduces new obstacle layouts and enemy placements to increase variety across the run.

### FR7: Score Tracking
The game tracks score for each run. Score is based primarily on how quickly each level is completed (faster completion yields higher score). Hearts remaining when a level is cleared add a fixed bonus per heart. The game stores a single overall high score. The high score persists between sessions via the save system.

### FR8: Game State Saving and Loading
When the player exits the game, the current state is saved to disk. Saved data includes: which levels have been completed, the overall high score, and any user settings. On launch, the game loads this saved state so the player can continue from where they left off. Save data is stored in a local file. There is one save slot.

### FR9: Character State Management
The mouse has a base state and may enter a modified state when a word power is active. State properties that can change include movement speed, jump height, hitbox size, and damage immunity. Only one modified state may be active at a time. The active power and its remaining duration are tracked and displayed on the HUD. When the duration expires, the mouse reverts to base state.

---

## 4. User Interface Requirements

### UIR1: Main Menu and Level Select
The main menu displays the game title, a Start button, and a high score display showing the player's best run score. Selecting Start loads Level 1. The level select screen (accessible after completing at least one level) shows all 3 levels as selectable tiles, locked or unlocked based on progress saved in FR8.

### UIR2: HUD — Letter Pool and Word Input
During gameplay, the HUD displays the player's current letter pool as individual letter tiles along the top of the screen (FR1). A word input area allows the player to select letters from the pool to spell a word. Submitting a valid word triggers the power described in FR2. Invalid submissions display a brief error indicator. The active word power name and a countdown timer are shown on the HUD (FR9).

### UIR3: HUD — Health and Score
The HUD displays the current heart count as heart icons (FR3). The current score and a running level timer are displayed (FR7). These update in real time.

### UIR4: Level Display
The level is rendered as a 2D side-scrolling view. The visible area scrolls horizontally to follow the mouse character. Platforms, walls, floors, and obstacles are rendered as distinct tile types. Letter tiles are visually distinct from terrain. The level boundaries are clearly indicated so the player can see where the room ends.

### UIR5: Character Display
The mouse character is rendered as a sprite. The sprite reflects the character's current state: base, powered-up (with visual indicator per active power), and damaged (brief flash or animation on taking a hit). The sprite faces the direction of movement.

### UIR6: Enemy Display
Enemies are rendered as distinct sprites that differ visually from the mouse and from level obstacles. Enemy patrol movement and chase movement are reflected in the sprite animation. Enemies display a brief animation on dealing damage.

### UIR7: Game Over and Level Complete Screens
When the mouse runs out of hearts, a Game Over screen displays the score earned in the current run and options to retry or return to the main menu. When a level is cleared, a Level Complete screen shows the level score, hearts remaining bonus, and time taken before transitioning to the next level or the final score screen.
