package com.letter.princess.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

public class Player {
    @Getter
    private final String name;
    @Getter
    @Setter
    private int token;
    @Getter
    @Setter
    private Hand currentHand;
    @Getter
    @Setter
    private boolean isImmune;

    public Player(String name) {
        this.name = name;
        this.setToken(0);
        this.setImmune(false);
    }

    public Card playerDrawsCard(Deck deck) {
        Card newCard = deck.draw();
        Collection<Card> cardsInHand = getCurrentHand().getCardsInHand();
        cardsInHand.add(newCard);
        return newCard;
    }

    public Card playerPicksCardToPlay(Card card) throws IllegalAccessException {
        return currentHand.removeCardFromHand(card);
    }
}
