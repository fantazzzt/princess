# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run

# Test (all)
./mvnw test

# Test (single class)
./mvnw test -Dtest=GameTest

# Compile only
./mvnw compile
```

## Conventions

### Testing

- **Prefer parameterized tests for near-identical cases.** When several tests share one body and differ only by inputs/expected values, write a single JUnit 5 `@ParameterizedTest` fed by a `@MethodSource`, rather than copy-pasted `@Test` methods. Pass a leading `String description` as the first argument and use `@ParameterizedTest(name = "{0}")` so each case reports under its own label. See `GameTest.isCurrentPlayer` and `GameEngineTest.validatePlayCard_throwsForInvalidPlay` for the pattern. A test that asserts a genuinely different shape (e.g. a success/no-throw path among rejection cases) stays its own `@Test`.

## Architecture

**Princess** is a Spring Boot REST API backend for the Love Letter card game. Java 24, Spring Boot 4.0.0, Maven.

### Layered Structure

```
Controllers (REST /game/**)
    ↓
GameService (@Service, manages lobby + active game lifecycle)
    ↓
GameBuilder + Game + GameEngine (core game logic)
    ↓
game/logic (per-card effect logic — currently empty stubs)
    ↓
Models (Player, Card, Deck, Role, Action, GameAction)
```

### Key Concepts

**State Machine** — `GameState` is a sealed interface with implementations: `Initializing`, `AwaitingDraw`, `AwaitingPlay`, `AwaitingEffectResolution`, `AwaitingEndTurn`, `GameOver`. Each implementation exposes a singleton constant (e.g. `AwaitingDraw.AWAITING_DRAW`). The game always holds exactly one state.

**GameBuilder** — Manages the lobby (2–6 players) and builds the `Game`. Players are identified by display name only (duplicates rejected); there is no player id yet (TODO for auth/db). `startGame()` builds the deck and player list, constructs the `Game` in `INITIALIZING`, then calls `Game.init()`, which removes the starting cards (1 hidden, or 3 for a 2-player game), deals 1 card to each player, and transitions to `AWAITING_DRAW`. Hardcoded to 5 tokens to win and a placeholder 6-card `Deck.testDeck()` — this is in-progress work.

**GameEngine** — Validates and executes player actions (`GameAction` records with an `Action` enum: `DRAW_CARD`, `PLAY_CARD`). A state→action map defines which action is legal in each state. `drawCard` is implemented (`AWAITING_DRAW` → `AWAITING_PLAY`); `validatePlayCard` is partial; `playCard` is a stub.

**GameService** — Spring `@Service` that holds a single `GameBuilder` and (once started) a single active `Game`. Not thread-safe (known TODO in source).

### REST API

Base path: `/game`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/new` | Create lobby |
| POST | `/add` | Add player to lobby |
| POST | `/remove` | Remove player from lobby |
| POST | `/start` | Start game |

There is no endpoint yet for viewing game state or submitting in-game actions.

### In-Progress Areas

- Card effect resolution: the `game/logic` package (`CardLogic`, `CardLogicLookup`, `PriestLogic`, `SpyLogic`) and the `PlayCardResult`/`CardData` models are empty stubs; `GameEngine.playCard` returns null.
- `Deck.testDeck()` is a placeholder 6-card deck.
- TODOs in source: shuffle player order at game start, map player count → tokens to win, race condition in `GameService.startGame`, per-game ids in the controller.

### Milestones

High-level plan; the detailed plan and design-decision log live in the maintainer's notes. Status as of 2026-07-01 — update this table when a milestone completes.

| # | Milestone                                                                          | Status |
|---|------------------------------------------------------------------------------------|--------|
| 0 | Cleanup of existing code                                                           | Done |
| 1 | Minimal `GameEngine` (`GameAction`, empty validate/play stubs)                     | Done |
| 2 | Player draws a card (`drawCard` in `Game`, start end-to-end test suite)            | **Current** |
| 3 | Implement 2 cards: Priest, Spy (`PriestLogic`, `SpyLogic`)                         | |
| 4 | Player plays a card (`validateCard`, state checks, `discardCard`)                  | |
| 5 | Card effects (Priest reveals seen card; Spy sets spy token)                        | |
| 6 | Ending a turn (`endTurn`, advance to next player; simulate a Priest/Spy-only game) | |
| 7 | Player loses round                                                                 | |
| 8 | Handmaid (immunity) + Baron (compare hands, lower card loses)          <br/>       | |
| 9 | End of round (empty deck / one player left; award tokens)                          | |
| 10 | Princess + Guard                                                                   | |
| 11 | End of game (token count vs. tokens to win)                                        | |
| 12 | Playing from localhost (controller endpoints, per-move logging)                    | |
| 13 | Game/player/card views                                                             | |
| 14 | 2-player games with information hiding                                             | |
| 15 | Event log                                                                          | |
| 16 | Advanced cards 1: King, Countess                                                   | |
| 17 | Advanced cards 2: Prince, Chancellor                                               | |
| 18 | `playerId` & authentication                                                        | |
| 19 | Database (persisting `Game`)                                                       | |
