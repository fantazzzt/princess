package com.letter.princess.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in a game of Princess
 */
public class Player {
    @Getter
    private final PlayerId playerId;
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
    @Setter
    private Hand hand;

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

    public Player(PlayerId id, String displayName) {
        this.playerId = id;
        this.displayName = displayName;
        this.numTokens = 0;
        this.setHand(new Hand());
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
        this.hand.addCard(card);
    }

    /**
     * Remove card from player's hand (e.g. when playing card)
     *
     * @return card that was removed
     */
    public Card removeCardFromHand(Card card) {
        return hand.removeCard(card);
    }

    // TODO: add test
    public List<Card> getDiscardedCards() {
        return new ArrayList<>(discardedCards);
    }
}
