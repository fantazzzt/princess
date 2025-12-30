package com.letter.princess.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private TestDeck deck;
    private Hand hand;

    @BeforeEach
    void setUp() {
        deck = new TestDeck(List.of(new Card(Card.Rank.PRIEST)));
        player = new Player("Alice");

        hand = new Hand();
        hand.setCardsInHand(new ArrayList<>());

        player.setCurrentHand(hand);
    }

    @Test
    void constructor_initializesDefaultsCorrectly() {
        assertEquals("Alice", player.getName());
        assertEquals(0, player.getToken());
        assertFalse(player.isImmune());
    }

    @Test
    void playerDrawsCard_addsCardToHand() {
        Card card = new Card(Card.Rank.GUARD);
        deck.setNextCard(card);

        Card drawnCard = player.playerDrawsCard(deck);

        assertEquals(card, drawnCard);
        assertEquals(1, hand.getCardsInHand().size());
        assertTrue(hand.getCardsInHand().contains(card));
    }

    @Test
    void playerPicksCardToPlay_removesCardFromHand() throws IllegalAccessException {
        Card card = new Card(Card.Rank.PRINCE);
        hand.getCardsInHand().add(card);

        Card playedCard = player.playerPicksCardToPlay(card);

        assertEquals(card, playedCard);
        assertFalse(hand.getCardsInHand().contains(card));
        assertEquals(0, hand.getCardsInHand().size());
    }

    @Test
    void playerPicksCardToPlay_throwsExceptionIfCardNotInHand() {
        Card cardInHand = new Card(Card.Rank.KING);
        Card cardNotInHand = new Card(Card.Rank.PRINCESS);

        hand.getCardsInHand().add(cardInHand);

        IllegalAccessException exception = assertThrows(
                IllegalAccessException.class,
                () -> player.playerPicksCardToPlay(cardNotInHand)
        );

        assertEquals(
                "You cannot play that card because it is not in the player's hand!",
                exception.getMessage()
        );
    }

    /**
     * Test stub for Deck
     * Remove once Deck is fully implemented
     */
    static class TestDeck extends Deck {
        private Card nextCard;

        public TestDeck(Collection<Card> deck) {
            super(deck);
        }

        void setNextCard(Card card) {
            this.nextCard = card;
        }

        @Override
        public Card draw() {
            return nextCard;
        }
    }
}
