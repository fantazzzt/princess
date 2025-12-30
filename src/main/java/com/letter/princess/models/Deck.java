package com.letter.princess.models;

import java.util.ArrayList;
import java.util.Collection;

public class Deck{

    private final Collection<Card> deck;

    public static Deck newDeck(){
        ArrayList<Card> myDeck = new ArrayList<>();
        return new Deck(myDeck);
    }

    public Deck(Collection<Card> deck) {
        this.deck = deck;
    }

    public Card draw(){
        return null;
    }

    public boolean isEmpty(){
        return deck.isEmpty();
    }

    public void play(){
    }

    public void shuffle(){
    }

}
