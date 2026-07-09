package com.letter.princess.game;

import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    private final List<Player> players;
    private final Deck deck;
    private final List<Card> removedCards;
    @Getter
    @Setter
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

    // ======== Game state ========

    // TODO: make it easier to go from Player -> their index in game?
    public boolean isCurrentPlayer(Player player) {
        return currentPlayerIndex == players.indexOf(player);
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    // ======== Game actions ========

    /**
     * Draw card to the player's hand.
     * Precondition: player is a valid player in the game.
     * Player doesnt have to be the current player (see Prince rules).
     * @param player Player to whose hand to draw card.
     * @return card that was drawn.
     */
    public Card drawCard(Player player) {
        Card drawnCard = deck.draw();
        player.addCardToHand(drawnCard);
        return drawnCard;
    }
}