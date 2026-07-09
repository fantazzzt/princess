package com.letter.princess.game;

import com.letter.princess.game.state.AwaitingEffectResolution;
import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Action;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.GameAction;
import com.letter.princess.models.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.letter.princess.game.state.AwaitingDraw.AWAITING_DRAW;
import static com.letter.princess.game.state.AwaitingEndTurn.AWAITING_END_TURN;
import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.game.state.GameOver.GAME_OVER;
import static com.letter.princess.game.state.Initializing.INITIALIZING;
import static com.letter.princess.models.Action.DRAW_CARD;
import static com.letter.princess.models.Action.PLAY_CARD;
import static com.letter.princess.models.Role.PRIEST;
import static com.letter.princess.models.Role.SPY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class GameEngineTest {

    private static final int NUM_TOKENS_TO_WIN = 5;
    private static final Card PRIEST_CARD = new Card(PRIEST);

    private final GameEngine engine = new GameEngine();

    /**
     * Build a game in the given state with the current turn at
     * currentPlayerIndex. validatePlayCard does not touch cards yet, so the
     * deck and removed pile are empty.
     */
    private static Game gameWith(GameState state, int currentPlayerIndex,
                                 List<Player> players) {
        return new Game(1, currentPlayerIndex, NUM_TOKENS_TO_WIN, players,
                new Deck(List.of()), new ArrayList<>(), state);
    }

    static Stream<Arguments> validateActionCases() {
        return Stream.of(
                arguments("DRAW_CARD is valid in AWAITING_DRAW",
                        AWAITING_DRAW, DRAW_CARD, true),
                arguments("PLAY_CARD is valid in AWAITING_PLAY",
                        AWAITING_PLAY, PLAY_CARD, true),
                arguments("PLAY_CARD is invalid in AWAITING_DRAW",
                        AWAITING_DRAW, PLAY_CARD, false),
                arguments("DRAW_CARD is invalid in AWAITING_PLAY",
                        AWAITING_PLAY, DRAW_CARD, false),
                arguments("no action is valid in INITIALIZING",
                        INITIALIZING, DRAW_CARD, false),
                arguments("no action is valid in AWAITING_EFFECT_RESOLUTION",
                        new AwaitingEffectResolution(), PLAY_CARD, false),
                arguments("no action is valid in AWAITING_END_TURN",
                        AWAITING_END_TURN, PLAY_CARD, false),
                arguments("no action is valid in GAME_OVER",
                        GAME_OVER, PLAY_CARD, false),
                arguments("missing action is invalid",
                        AWAITING_DRAW, null, false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateActionCases")
    public void validateAction(String description, GameState state,
                               Action action, boolean expected) {
        Player alice = new Player("alice");
        Game game = gameWith(state, 0, List.of(alice, new Player("bob")));
        GameAction gameAction = new GameAction(action, PRIEST_CARD, "bob", null);

        assertEquals(expected, engine.validateAction(game, gameAction));
    }

    @Test
    public void drawCard_drawsToHandAndTransitionsToAwaitingPlay() {
        Player alice = new Player("alice");
        Player bob = new Player("bob");
        Deck deck = new Deck(List.of(PRIEST_CARD, new Card(SPY)));
        Game game = new Game(1, 0, NUM_TOKENS_TO_WIN, List.of(alice, bob),
                deck, new ArrayList<>(), AWAITING_DRAW);

        Card drawn = engine.drawCard(game, alice);

        assertEquals(PRIEST_CARD, drawn);
        assertEquals(List.of(PRIEST_CARD), alice.getHand());
        assertEquals(AWAITING_PLAY, game.getGameState());
        assertEquals(1, deck.getSize());
    }

    @Test
    public void drawCard_throwsOnEmptyDeckAndLeavesGameUntouched() {
        Player alice = new Player("alice");
        Game game = gameWith(AWAITING_DRAW, 0,
                List.of(alice, new Player("bob")));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> engine.drawCard(game, alice));

        assertTrue(ex.getMessage().contains("empty"),
                "expected message containing 'empty' but got: "
                        + ex.getMessage());
        assertEquals(AWAITING_DRAW, game.getGameState());
        assertEquals(List.of(), alice.getHand());
    }

    @Test
    public void validatePlayCard_passesForCurrentPlayerInAwaitingPlay() {
        Player alice = new Player("alice");
        Player bob = new Player("bob");
        Game game = gameWith(AWAITING_PLAY, 0, List.of(alice, bob));
        GameAction action = new GameAction(PLAY_CARD, PRIEST_CARD, "bob", null);

        engine.validatePlayCard(game, alice, action);
    }

    static Stream<Arguments> invalidPlayCases() {
        Player alice = new Player("alice");
        Player bob = new Player("bob");
        Player stranger = new Player("stranger");
        List<Player> players = List.of(alice, bob);
        GameAction play = new GameAction(PLAY_CARD, PRIEST_CARD,"bob", null);
        // Non-play requests are caught by the validateAction step, which
        // reports them as a state mismatch (the trailing PLAY_CARD check in
        // validatePlayCard is unreachable behind it).
        GameAction missingAction = new GameAction(null, PRIEST_CARD,"bob",null);
        GameAction draw = new GameAction(DRAW_CARD, null, null, null);

        return Stream.of(
                arguments("state AWAITING_DRAW is rejected",
                        gameWith(AWAITING_DRAW, 0, players), alice, play, "AwaitingPlay"),
                arguments("state INITIALIZING is rejected",
                        gameWith(INITIALIZING, 0, players), alice, play, "AwaitingPlay"),
                arguments("state GAME_OVER is rejected",
                        gameWith(GAME_OVER, 0, players), alice, play, "AwaitingPlay"),
                arguments("player playing out of turn is rejected",
                        gameWith(AWAITING_PLAY, 0, players), bob, play, "turn"),
                // The engine assumes membership was checked upstream, so a
                // stranger falls through to the turn check, not its own error.
                arguments("player not in the game is rejected",
                        gameWith(AWAITING_PLAY, 0, players), stranger, play, "turn"),
                arguments("missing action is rejected",
                        gameWith(AWAITING_PLAY, 0, players), alice, missingAction, "AwaitingPlay"),
                arguments("draw action is rejected",
                        gameWith(AWAITING_PLAY, 0, players), alice, draw, "AwaitingPlay"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidPlayCases")
    public void validatePlayCard_throwsForInvalidPlay(String description,
                                                      Game game, Player player,
                                                      GameAction action,
                                                      String expectedMessage) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> engine.validatePlayCard(game, player, action));
        assertTrue(ex.getMessage().contains(expectedMessage),
                "expected message containing '" + expectedMessage
                        + "' but got: " + ex.getMessage());
    }
}
