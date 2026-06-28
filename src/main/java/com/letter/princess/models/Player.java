package com.letter.princess.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in a game of Princess
 * TODO: go back to playerId when we need it for auth/db
 */
public class Player {
    @Getter
    private final String displayName;
    /**
     * Number of tokens player has acquired in current game
     */
    @Getter
    private int numTokens;
    /**
     * Player's current hand (should be 1 card if it's not their turn)
     */
    @Getter
    private List<Card> hand;

    /**
     * Whether player is still in the current round
     */
    @Getter
    @Setter
    private boolean isInRound;

    /**
     * Whether player is currently immune to being chosen as target by
     * another player (e.g. they played a Handmaid)
     */
    @Getter
    @Setter
    private boolean isImmune;

    @Getter
    @Setter
    private boolean hasPlayedSpyThisRound;

    /**
     * Player's discarded hands in order (first = oldest discarded)
     */
    private final List<Card> discardedCards;

    public Player(String displayName) {
        this.displayName = displayName;
        this.numTokens = 0;
        this.hand = new ArrayList<>();
        this.setImmune(false);
        this.discardedCards = new ArrayList<>();
        this.isInRound = true;
    }

    /**
     * Give the player a token (used at end of round)
     */
    public void addToken() {
        this.numTokens++;
    }

    /**
     * Add card to player's hand (e.g. after drawing from deck)
     *
     * @param card
     */
    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    /**
     * Remove card from player's hand (e.g. when playing card)
     *
     * @return removed card (for convenience
     * @throws IllegalArgumentException if card isn't in hand
     */
    public Card removeCardFromHand(Card card) {
        if (!hand.remove(card)) {
            throw new IllegalArgumentException("Attempted to remove card that" +
                    " wasn't in hand");
        }
        return card;
    }

    // TODO: add test
    public List<Card> getDiscardedCards() {
        return new ArrayList<>(discardedCards);
    }
}
