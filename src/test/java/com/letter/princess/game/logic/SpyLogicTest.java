package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
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

import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.models.Action.PLAY_CARD;
import static com.letter.princess.models.Role.PRIEST;
import static com.letter.princess.models.Role.SPY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SpyLogicTest {

    private static final int NUM_TOKENS_TO_WIN = 5;
    private static final Card SPY_CARD = new Card(SPY);
    private static final Card PRIEST_CARD = new Card(PRIEST);

    private final SpyLogic logic = new SpyLogic();

    /**
     * Minimal game with the given player at the current turn, in AWAITING_PLAY
     * (the state a card is played from). SpyLogic ignores the deck/removed
     * pile, so those are empty.
     */
    private static Game awaitingPlayGameFor(Player player) {
        return new Game(1, 0, NUM_TOKENS_TO_WIN, List.of(player),
                new Deck(List.of()), new ArrayList<>(), AWAITING_PLAY);
    }

    static Stream<Arguments> isValidCases() {
        return Stream.of(
                arguments("SPY with no target and no guess is valid",
                        new GameAction(PLAY_CARD, SPY_CARD, null, null), true),
                arguments("non-SPY card is rejected",
                        new GameAction(PLAY_CARD, PRIEST_CARD, null, null), false),
                arguments("SPY with a target is rejected",
                        new GameAction(PLAY_CARD, SPY_CARD, "bob", null), false),
                arguments("SPY with a guess is rejected",
                        new GameAction(PLAY_CARD, SPY_CARD, null, PRIEST), false),
                arguments("SPY with a target and a guess is rejected",
                        new GameAction(PLAY_CARD, SPY_CARD, "bob", PRIEST), false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("isValidCases")
    public void isValidPlayCardAction(String description, GameAction action,
                                      boolean expected) {
        Player alice = new Player("alice");
        Game game = awaitingPlayGameFor(alice);
        assertEquals(expected,
                logic.isValidPlayCardAction(game, alice, action));
    }

    @Test
    public void apply_setsSpyFlagAndReturnsTrue() {
        Player alice = new Player("alice");
        Game game = awaitingPlayGameFor(alice);
        GameAction action = new GameAction(PLAY_CARD, SPY_CARD, null, null);
        assertFalse(alice.isPlayedSpyThisRound(),
                "player should not start with the spy flag set");

        boolean result = logic.apply(game, alice, action);

        assertTrue(result);
        assertTrue(alice.isPlayedSpyThisRound());
    }
}
