package com.letter.princess.game;

import com.letter.princess.game.state.GameState;
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
import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.game.state.GameOver.GAME_OVER;
import static com.letter.princess.game.state.Initializing.INITIALIZING;
import static com.letter.princess.models.Action.PLAY_CARD;
import static com.letter.princess.models.Role.PRIEST;
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
        // PLAY_CARD is the only Action constant, so a null action is the only
        // way a request can fail to be a play.
        GameAction missingAction = new GameAction(null, PRIEST_CARD,"bob",null);

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
                arguments("non-play action is rejected",
                        gameWith(AWAITING_PLAY, 0, players), alice, missingAction, "play"));
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
