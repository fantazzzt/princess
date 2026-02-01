package com.letter.princess.views;

/**
 * An explicit type for a player's view of a card.
 * A card can be known or hidden
 */
sealed public interface CardView permits KnownCard, HiddenCard {}

