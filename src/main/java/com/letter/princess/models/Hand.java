package com.letter.princess.models;

import java.util.ArrayList;
import java.util.Collection;

public class Hand {
    private final Collection<Card> currentHand;

    /**
     * Create a new, empty hand
     */
    public Hand() {
        this.currentHand = new ArrayList<>();
    }

    /**
     * Get the cards in the current hand
     * @return collection of cards in hand (can be safely modified)
     */
    public Collection<Card> getCardsInHand() {
        return new ArrayList<>(currentHand);
    }

    /**
     * Add card to the current hand
     * @param card Card to add
     */
    public void addCard(Card card) {
        currentHand.add(card);
    }

    public Card removeCard(Card card) {
        if (!currentHand.contains(card)) {
            throw new IllegalArgumentException("You cannot play that card " +
                    "because it is not in the player's hand!");
        }
        currentHand.remove(card);
        return card;
    }
}
