package com.letter.princess.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(new PlayerId("AliceId"), "Alice");
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
        Hand hand = player.getHand();
        Collection<Card> currCards = hand.getCardsInHand();
        assertEquals(1, currCards.size());
        assertTrue(currCards.contains(card));
    }

    @Test
    public void testRemoveCardFromHand() throws IllegalAccessException {
        Card card = new Card(Role.PRINCE);
        player.addCardToHand(card);
        Card removedCard = player.removeCardFromHand(card);
        assertEquals(card, removedCard);
        assertFalse(player.getHand().getCardsInHand().contains(card));
        assertEquals(0, player.getHand().getCardsInHand().size());
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
                "You cannot play that card because it is not in the player's hand!",
                exception.getMessage()
        );
    }
}
