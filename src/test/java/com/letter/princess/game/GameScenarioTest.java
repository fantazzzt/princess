package com.letter.princess.game;

import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.GameAction;
import com.letter.princess.models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.letter.princess.game.state.AwaitingDraw.AWAITING_DRAW;
import static com.letter.princess.game.state.AwaitingEndTurn.AWAITING_END_TURN;
import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.models.Action.PLAY_CARD;
import static com.letter.princess.models.Role.PRIEST;
import static com.letter.princess.models.Role.SPY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Scenario tests: drive a whole game through GameBuilder + GameEngine the way a
 * client would, over a *deterministic* deck, asserting state after each step.
 * In-memory only (HTTP end-to-end tests arrive with the controller in
 * milestone 12).
 */
public class GameScenarioTest {

    private final GameEngine engine = new GameEngine();

    @Test
    public void twoPlayerGame_startThenCurrentPlayerDraws() {
        // A 2-player start removes the first 3 cards, then deals one to each
        // player in order; the last card is what the current player draws. Hold
        // references so we can assert the exact cards, not just hand sizes.
        Card olgaStart = new Card(PRIEST);
        Card cadenStart = new Card(SPY);
        Card olgaDraws = new Card(PRIEST);
        Deck deck = new Deck(List.of(
                new Card(SPY), new Card(SPY), new Card(SPY), // removed
                olgaStart,                                   // -> olga's hand
                cadenStart,                                  // -> caden's hand
                olgaDraws));                                 // -> olga draws

        Game game = GameBuilder.initializeGame()
                .addPlayer("olga").addPlayer("caden")
                .startGameWithDeck(deck);
        Player olga = game.getPlayers().get(0);
        Player caden = game.getPlayers().get(1);

        // The real start flow has already run: cards removed + dealt, now
        // waiting for the first player to draw.
        assertEquals(AWAITING_DRAW, game.getGameState());
        assertEquals(List.of(olgaStart), olga.getHand());
        assertEquals(List.of(cadenStart), caden.getHand());

        // Current player (olga) draws the last card.
        Card drawn = engine.drawCard(game, olga);

        assertEquals(olgaDraws, drawn);
        assertEquals(List.of(olgaStart, olgaDraws), olga.getHand());
        assertEquals(List.of(cadenStart), caden.getHand()); // caden untouched
        assertEquals(AWAITING_PLAY, game.getGameState());
        assertTrue(game.isDeckEmpty());                      // all 6 cards used
    }

    @Test
    public void twoPlayerGame_currentPlayerDrawsThenPlaysSpy() {
        // Same start as above, but olga draws a Spy she can then play. Roles
        // differ (Priest vs Spy) so we can assert exactly which card is
        // discarded and which stays in hand.
        Card olgaStart = new Card(PRIEST);
        Card cadenStart = new Card(SPY);
        Card olgaSpy = new Card(SPY);
        Deck deck = new Deck(List.of(
                new Card(SPY), new Card(SPY), new Card(SPY), // removed
                olgaStart,                                   // -> olga's hand
                cadenStart,                                  // -> caden's hand
                olgaSpy));                                   // -> olga draws

        Game game = GameBuilder.initializeGame()
                .addPlayer("olga").addPlayer("caden")
                .startGameWithDeck(deck);
        Player olga = game.getPlayers().get(0);
        Player caden = game.getPlayers().get(1);

        engine.drawCard(game, olga);
        assertEquals(AWAITING_PLAY, game.getGameState());

        // Olga plays her Spy the way a client would: validate, then execute.
        GameAction playSpy = new GameAction(PLAY_CARD, olgaSpy, null, null);
        engine.validatePlayCard(game, olga, playSpy);
        engine.playCard(game, olga, playSpy);

        // The Spy moved to the discard pile; her starting Priest remains.
        assertEquals(List.of(olgaStart), olga.getHand());
        assertEquals(List.of(olgaSpy), olga.getDiscardedCards());
        assertTrue(olga.isPlayedSpyThisRound());     // Spy effect applied
        assertEquals(List.of(cadenStart), caden.getHand()); // caden untouched
        assertEquals(AWAITING_END_TURN, game.getGameState());
    }
}
