package com.letter.princess.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private static final Card PRIEST = new Card(Role.PRIEST);
    private static final Card PRINCESS = new Card(Role.PRINCESS);
    private static final List<Card> SAMPLE_CARDS = List.of(
            PRIEST, PRINCESS
    );

    private Deck deck;

    @BeforeEach
    public void setup() {
        deck = new Deck(SAMPLE_CARDS);
    }

    @Test
    public void testNewDeckHasDefaultCards() {
        deck = Deck.newDeck();
        assertEquals(3, deck.getSize());
        List<Card> expectedCards = List.of(new Card(Role.PRIEST), new Card(Role.PRINCESS), new Card(Role.PRIEST));
        List<Card> deckCards = deck.getAllCards();
        for (int i = 0; i < expectedCards.size(); i++) {
            assertEquals(expectedCards.get(i), deckCards.get(i));
        }
    }

    @Test
    public void testIsEmpty() {
        assertFalse(deck.isEmpty());
    }

    @Test
    public void testGetSize() {
        assertEquals(SAMPLE_CARDS.size(), deck.getSize());
    }

    @Test
    public void testDraw() {
        Card topCard = deck.draw();
        assertEquals(SAMPLE_CARDS.get(0), topCard);
        topCard = deck.draw();
        assertEquals(SAMPLE_CARDS.get(1), topCard);
        assertTrue(deck.isEmpty());
    }

    @Test
    public void testShuffle() {
        deck.shuffle();
        List<Card> results = deck.getAllCards();
        assertTrue(results.containsAll(SAMPLE_CARDS));
        assertTrue(SAMPLE_CARDS.containsAll(results));
    }
}
