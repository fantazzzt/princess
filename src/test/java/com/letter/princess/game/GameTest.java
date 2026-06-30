package com.letter.princess.game;

import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import com.letter.princess.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.letter.princess.game.state.AwaitingDraw.AWAITING_DRAW;
import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.game.state.Initializing.INITIALIZING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class GameTest {

    private static final Card PRIEST_1 = new Card(Role.PRIEST);
    private static final Card PRIEST_2 = new Card(Role.PRIEST);
    private static final Card SPY_1 = new Card(Role.SPY);
    private static final Card SPY_2 = new Card(Role.SPY);
    private static final Card SPY_3 = new Card(Role.SPY);

    private static final int NUM_TOKENS_TO_WIN = 5;

    /**
     * Build a game with fixed round/player/token values so each test only has
     * to express what it cares about: the players, deck, removed pile and state.
     */
    private Game newGame(List<Player> players, Deck deck,
                         List<Card> removedCards, GameState state) {
        return new Game(1, 0, NUM_TOKENS_TO_WIN, players, deck, removedCards,
                state);
    }

    @Test
    public void initTwoPlayer_removesThreeCardsAndDealsOneEach() {
        Player alice = new Player("alice");
        Player bob = new Player("bob");
        Deck deck = new Deck(List.of(
                PRIEST_1, PRIEST_2, SPY_1, SPY_2, SPY_3));
        List<Card> removed = new ArrayList<>();
        Game game = newGame(List.of(alice, bob), deck, removed, INITIALIZING);

        game.init();

        // 2-player game removes the first 3 cards...
        assertEquals(List.of(PRIEST_1, PRIEST_2, SPY_1), removed);
        // ...then deals one card to each player in order...
        assertEquals(List.of(SPY_2), alice.getHand());
        assertEquals(List.of(SPY_3), bob.getHand());
        // ...and nothing more is drawn.
        assertEquals(0, deck.getSize());
        assertEquals(AWAITING_DRAW, game.getGameState());
    }

    @Test
    public void initThreePlayer_removesOneCardAndDealsOneEach() {
        Player alice = new Player("alice");
        Player bob = new Player("bob");
        Player carol = new Player("carol");
        Deck deck = new Deck(List.of(PRIEST_1, SPY_1, SPY_2, SPY_3));
        List<Card> removed = new ArrayList<>();
        Game game = newGame(List.of(alice, bob, carol), deck, removed,
                INITIALIZING);

        game.init();

        // 3+-player game removes only the first card...
        assertEquals(List.of(PRIEST_1), removed);
        // ...then deals one card to each player in order.
        assertEquals(List.of(SPY_1), alice.getHand());
        assertEquals(List.of(SPY_2), bob.getHand());
        assertEquals(List.of(SPY_3), carol.getHand());
        assertEquals(0, deck.getSize());
        assertEquals(AWAITING_DRAW, game.getGameState());
    }

    @Test
    public void initThrowsWhenStateIsNotInitializing() {
        Deck deck = new Deck(List.of(PRIEST_1, SPY_1, SPY_2));
        Game game = newGame(List.of(new Player("alice"), new Player("bob")),
                deck, new ArrayList<>(), AWAITING_DRAW);

        assertThrows(IllegalStateException.class, game::init);
    }

    /**
     * Build a game in the given turn-state. isCurrentPlayer only reads
     * currentPlayerIndex + players, so deck/removed pile are empty here.
     */
    private static Game gameAtTurn(int currentPlayerIndex, List<Player> players) {
        return new Game(1, currentPlayerIndex, NUM_TOKENS_TO_WIN, players,
                new Deck(List.of()), new ArrayList<>(), AWAITING_PLAY);
    }

    static Stream<Arguments> isCurrentPlayerCases() {
        Player alice = new Player("alice");
        Player bob = new Player("bob");
        Player stranger = new Player("stranger");
        List<Player> players = List.of(alice, bob);
        return Stream.of(
                arguments("player at the current index is current",
                        0, players, alice, true),
                arguments("another player is not current",
                        0, players, bob, false),
                arguments("current player follows currentPlayerIndex",
                        1, players, bob, true),
                arguments("player not in the game is not current",
                        0, players, stranger, false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("isCurrentPlayerCases")
    public void isCurrentPlayer(String description, int currentPlayerIndex,
                                List<Player> players, Player candidate,
                                boolean expected) {
        Game game = gameAtTurn(currentPlayerIndex, players);

        assertEquals(expected, game.isCurrentPlayer(candidate));
    }
}
