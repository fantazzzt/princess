package com.letter.princess.views;

import com.letter.princess.models.Card;

/**
 * An explicit type for a player's view of a card.
 * A card can be known or hidden
 */
sealed public interface CardView permits KnownCard, HiddenCard {
    static CardView known(Card card) {
        return new KnownCard(card);
    }

    static CardView hidden() {
        return HiddenCard.HIDDEN_CARD;
    }
}

