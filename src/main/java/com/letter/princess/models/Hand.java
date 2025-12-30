package com.letter.princess.models;

import java.util.Collection;

public class Hand {
    private Collection<Card> currentHand;


    public Collection<Card> getCardsInHand() {
        return currentHand;
    }

    public void setCardsInHand(Collection<Card> currentHand) {
        this.currentHand = currentHand;
    }

    public Card removeCardFromHand(Card card) throws IllegalAccessException {
        if (!currentHand.contains(card)) {
            throw new IllegalAccessException("You cannot play that card because it is not in the player's hand!");
        }
        currentHand.remove(card);
        return card;
    }
}
