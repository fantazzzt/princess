package com.letter.princess.game;

import com.letter.princess.game.state.AwaitingDraw;
import com.letter.princess.game.state.AwaitingPlay;
import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.letter.princess.game.state.AwaitingDraw.AWAITING_DRAW;
import static com.letter.princess.game.state.Initializing.INITIALIZING;

/**
 * Represents a complete game of Princess
 */
@AllArgsConstructor
public class Game {

    @Getter
    private int currentRound;
    @Getter
    private int currentPlayerIndex;
    @Getter
    private final int numTokensToWin;
    private final List<Player> players;
    private final Deck deck;
    private final List<Card> removedCards;
    @Getter
    private GameState gameState;

    // ======== Game init ========
    /**
     * Initialize game from INITIALIZING -> AWAITING_DRAW state
     */
    void init() {
        if (!INITIALIZING.equals(gameState)) {
            throw new IllegalStateException("State must be INITIALIZING to " +
                    "call init");
        }
        removeStartingCards();
        drawInitialHands();
        gameState = AWAITING_DRAW;
    }

    /**
     * Remove the starting 1 hidden card (3+ player game) or 3 revealed cards
     * (2 player game) when init game
     */
    private void removeStartingCards() {
        removedCards.add(deck.draw());
        if (players.size() == 2) {
            // 3 total removed cards
            removedCards.add(deck.draw());
            removedCards.add(deck.draw());
        }
    }

    /**
     * Draw the initial hand for each player when init game
     */
    private void drawInitialHands() {
        for (Player player : players) {
            player.addCardToHand(deck.draw());
        }
    }

    // ======== Game init ========

    /**
     * Draw a card to the given player's hand
     * @param player Current player (must be validated before this method)
     * @return Card that was drawn
     * @throws IllegalStateException if game state is not awaiting draw or if
     * deck has no cards
     */
    public Card drawCard(Player player) {
        if (!gameState.equals(AWAITING_DRAW)) {
            throw new IllegalStateException("Cannot draw card");
        }
        if (deck.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        Card card = deck.draw();
        player.addCardToHand(card);
        gameState = new AwaitingPlay(currentPlayerIndex);
        return card;
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    List<Player> getPlayers() {
        return players;
    }
}
