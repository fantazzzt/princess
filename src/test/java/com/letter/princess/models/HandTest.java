package com.letter.princess.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    private Hand hand;
    private final Card PRIEST = new Card(Role.PRIEST);
    private final Card PRINCESS = new Card(Role.PRINCESS);

    @BeforeEach
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testCreateHand() {
        Collection<Card> cards = hand.getCardsInHand();
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testAddAndRemoveCard() {
        hand.addCard(PRIEST);
        hand.addCard(PRINCESS);
        hand.addCard(PRIEST);
        Collection<Card> currentCards = hand.getCardsInHand();
        assertEquals(3, currentCards.size());
        assertTrue(currentCards.contains(PRIEST));
        assertTrue(currentCards.contains(PRINCESS));
        hand.removeCard(PRIEST);
        currentCards = hand.getCardsInHand();
        assertEquals(2, currentCards.size());
        assertTrue(currentCards.contains(PRIEST));
        assertTrue(currentCards.contains(PRINCESS));
        hand.removeCard(PRIEST);
        currentCards = hand.getCardsInHand();
        assertEquals(1, currentCards.size());
        assertTrue(currentCards.contains(PRINCESS));
        hand.removeCard(PRINCESS);
        currentCards = hand.getCardsInHand();
        assertTrue(currentCards.isEmpty());
    }

    @Test
    public void testRemoveNonexistentCard_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> hand.removeCard(PRIEST));
    }
}
