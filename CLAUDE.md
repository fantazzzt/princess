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
- **Scenario tests drive whole games, not single methods.** `GameScenarioTest` starts a real game over a *deterministic* deck and drives it through `GameBuilder` + `GameEngine` the way a client would, asserting the exact cards/state after each step (not just sizes — a fixed deck exists so you can assert *which* card lands where). It lives in package `com.letter.princess.game` so it can reach package-private `Game.init()` / `GameBuilder.startGameWithDeck` — note Java package-private does **not** extend to subpackages, so this is the only package that works. Keep per-class unit tests separate; scenario tests are the multi-step integration tier. True HTTP end-to-end tests arrive with the controller (M12).

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
game/logic (per-card effect logic — Spy wired via CardLogicLookup; other cards stubbed)
    ↓
Models (Player, Card, Deck, Role, Action, GameAction)
```

### Key Concepts

**State Machine** — `GameState` is a sealed interface with implementations: `Initializing`, `AwaitingDraw`, `AwaitingPlay`, `AwaitingEffectResolution`, `AwaitingEndTurn`, `GameOver`. Each implementation exposes a singleton constant (e.g. `AwaitingDraw.AWAITING_DRAW`). The game always holds exactly one state.

**GameBuilder** — Manages the lobby (2–6 players) and builds the `Game`. Players are identified by display name only (duplicates rejected); there is no player id yet (TODO for auth/db). `startGame()` builds the deck and player list, constructs the `Game` in `INITIALIZING`, then calls `Game.init()`, which removes the starting cards (1 hidden, or 3 for a 2-player game), deals 1 card to each player, and transitions to `AWAITING_DRAW`. A package-private `startGameWithDeck(Deck)` runs the *same* flow over a caller-supplied deck (used in its given order, no shuffle) so scenario tests can start a real game from a known deck without reimplementing the start sequence. Hardcoded to 5 tokens to win and a placeholder 6-card `Deck.testDeck()` — this is in-progress work.

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

- Card effect resolution: the `CardLogic` interface (`isValidPlayCardAction` + `apply`), `SpyLogic` (sets the player's played-spy-this-round flag), and `CardLogicLookup` (`Role` → `CardLogic`, currently only SPY) are implemented and unit-tested (`SpyLogicTest`, `CardLogicLookupTest`). `PriestLogic` and the `CardData` model are still empty stubs, and `GameEngine.playCard` still returns null — it does not yet dispatch through `CardLogicLookup` (that lands in M4).
- **Action + event model (designed, not built).** The plan: a single `POST /game/{id}/action` carries a discriminated `GameAction` into one uniform `GameEngine.execute(game, action)`. The response is **action-agnostic** (success + the log's head sequence), *not* a per-card result — the `PlayCardResult` stub is superseded by this. An action's consequences are emitted as **`GameEvent`s** (sealed interface + record per type: `CardPlayed`, `CardRevealed`, …) wrapped in a `LoggedEvent(seq, …)` envelope with a monotonic per-game sequence, appended to an `EventLog` and read by cursor (`?after=seq`). Clients render the sequence as ordered beats (progressive display); polling first, server-push (SSE) later. Minimal `GameEvent`/`EventLog` lands in M5. (Full rationale in the maintainer's notes.)
- `Deck.testDeck()` is a placeholder 6-card deck; `GameBuilder.startGameWithDeck(Deck)` injects a fixed deck for deterministic scenario tests.
- TODOs in source: shuffle deck + player order at game start, map player count → tokens to win, race condition in `GameService.startGame`, per-game ids in the controller.

### Milestones

High-level plan mirrored from the maintainer's notes (`coding/Princess notes.md`), which hold the detailed plan and design-decision log and are the source of truth. Status as of 2026-07-09 — update this table when a milestone completes.

| # | Milestone                                                                          | Status |
|---|------------------------------------------------------------------------------------|--------|
| 0 | Cleanup of existing code                                                           | Done |
| 1 | Minimal `GameEngine` (`GameAction`, empty validate/play stubs)                     | Done |
| 2 | Player draws a card (`drawCard` in `Game`/`GameEngine`, scenario test suite)       | Done |
| 3 | First card: Spy (`CardLogic` interface, `SpyLogic` sets spy token)                 | Done |
| 4 | Player plays a card (`playCard`, `discardCard`, scenario test)                     | **Current** |
| 5 | Card effect + event log: Priest + minimal `GameEvent`/`EventLog` (Priest reveals target's card; actions emit events; valid-target check) | |
| 6 | Ending a turn (`endTurn`, advance to next active player; simulate a Priest/Spy-only game) | |
| 7 | Player loses round                                                                 | |
| 8 | Handmaid (immunity) + Baron (compare hands, lower card loses)                      | |
| 9 | End of round (empty deck / one player left; award tokens + spy token)             | |
| 10 | Princess + Guard                                                                   | |
| 11 | End of game (token count vs. tokens to win)                                        | |
| 12 | Playing from localhost (controller endpoints, per-move logging)                    | |
| 13 | Game/player/card views                                                             | |
| 14 | 2-player games with information hiding                                             | |
| 15 | Advanced cards 1: King, Countess                                                   | |
| 16 | Advanced cards 2: Prince, Chancellor                                               | |
| 17 | `playerId` & authentication (`Player` value-equality on id)                        | |
| 18 | Database (persisting `Game`; event log in its own table)                          | |
