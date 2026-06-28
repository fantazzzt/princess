package com.letter.princess.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Alice");
    }

    @Test
    public void testInitializesDefaultsCorrectly() {
        assertEquals("Alice", player.getDisplayName());
        assertEquals(0, player.getNumTokens());
        assertFalse(player.isImmune());
    }

    @Test
    public void testAddToken() {
        player.addToken();
        assertEquals(1, player.getNumTokens());
        player.addToken();
        assertEquals(2, player.getNumTokens());
    }

    @Test
    public void testAddCardToHand() {
        Card card = new Card(Role.GUARD);

        player.addCardToHand(card);
        List<Card> hand = player.getHand();
        assertEquals(1, hand.size());
        assertTrue(hand.contains(card));
    }

    @Test
    public void testRemoveCardFromHand() {
        Card card = new Card(Role.PRINCE);
        player.addCardToHand(card);
        List<Card> expectedHand = new ArrayList<>();
        expectedHand.add(card);
        assertEquals(expectedHand, player.getHand());
        assertEquals(card, player.removeCardFromHand(card));
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    public void testRemoveCardFromHand_throwsExceptionIfCardNotInHand() {
        Card cardInHand = new Card(Role.KING);
        Card cardNotInHand = new Card(Role.PRINCESS);

        player.addCardToHand(cardInHand);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> player.removeCardFromHand(cardNotInHand)
        );

        assertEquals(
                "Attempted to remove card that wasn't in hand",
                exception.getMessage()
        );
    }
}
