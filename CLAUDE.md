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
Game + GameBuilder (core game logic)
    ↓
Models (Player, Card, Deck, Hand, Role)
    ↓
Views (POV-filtered serialization for REST responses)
```

### Key Concepts

**State Machine** — `GameState` is a sealed interface with implementations: `AwaitingDraw`, `AwaitingPlay`, `AwaitingEffectResolution`, `AwaitingEndTurn`, `GameOver`. The game always holds exactly one state.

**POV Filtering** — `Game.gameView(playerId)` returns a `GameView` where the requesting player's hand is `KnownCard` instances and all other players' hands are `HiddenCard`. This is the only information-hiding mechanism. `CardView` is a sealed interface for `KnownCard` (record) and `HiddenCard` (singleton enum).

**Player Identity** — `PlayerId` wraps both an internal `int` id and a `UUID` derived from the player's name. `PlayerIdManager` is a singleton that manages the global mapping. Players are identified externally by UUID string.

**GameBuilder** — Builder that manages the lobby (2–6 players) and initializes a `Game`. Calls `startGame()` to transition from lobby → active game. Hardcoded to 5 tokens to win and a minimal deck (2 Priests + 1 Princess) — this is in-progress work.

**GameService** — Spring `@Service` that holds a single `GameBuilder` and (once started) a single active `Game`. Not thread-safe (known TODO in source).

### REST API

Base path: `/game`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/new` | Create lobby |
| POST | `/add` | Add player to lobby |
| POST | `/remove` | Remove player from lobby |
| POST | `/start` | Start game |
| GET | `/{playerId}` | Get game view for player |
| POST | `/action` | Play a card (incomplete) |

### In-Progress Areas

Card effect resolution is unimplemented — `AwaitingEffectResolution` and the `/action` endpoint exist as stubs. The deck initialization in `GameBuilder` is placeholder (3 cards). Multiple TODOs exist for: random player order, event logging, race conditions in `GameService`, and supporting duplicate player names.
