package com.letter.princess.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of cards
 */
public class Deck {

    private final List<Card> deck;

    /**
     * Build a new default deck, currently empty
     *
     * @return new deck
     */
    public static Deck testDeck() {
        List<Card> defaultStartingCards = new ArrayList<>();
        defaultStartingCards.add(new Card(Role.PRIEST));
        defaultStartingCards.add(new Card(Role.PRINCESS));
        defaultStartingCards.add(new Card(Role.PRIEST));
        defaultStartingCards.add(new Card(Role.SPY));
        defaultStartingCards.add(new Card(Role.SPY));
        defaultStartingCards.add(new Card(Role.HANDMAID));
        return new Deck(defaultStartingCards);
    }

    /**
     * Initialise a new deck with the given list of cards
     * Makes a copy of the passed-in deck
     *
     * @param deck List of cards to put in the deck, can be safely modified
     *             without affecting the new Deck
     */
    public Deck(List<Card> deck) {
        this.deck = new ArrayList<>(deck);
    }

    /**
     * @return top card of deck
     * @throws java.util.NoSuchElementException if deck is empty
     */
    public Card draw() {
        return deck.removeFirst();
    }

    /**
     * @return true if deck is empty
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * @return number of cards remaining in deck
     */
    public int getSize() {
        return deck.size();
    }

    /**
     * Get a copy of the entire deck (returned list can be safely modified)
     *
     * @return list of all cards in the deck, in order of their appearance
     */
    public List<Card> getAllCards() {
        return new ArrayList<>(deck);
    }

    /**
     * Shuffle deck randomly
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    // TODO: addCardToBottom(card)
}
