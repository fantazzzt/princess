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

    /**
     * Draw a card to the given player's hand
     * @param player Current player (must be validated before this method)
     * @return Card that was drawn
     * @throws IllegalStateException if game state is not awaiting draw or if
     * deck has no cards
     */
    public Card drawCard(Player player) {
        if (!gameState.equals(AwaitingDraw.AWAITING_DRAW)) {
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
