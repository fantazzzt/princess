package com.letter.princess.views;

import com.letter.princess.models.Card;

/**
 * Represents a card whose identity is known to the viewer
 * @param card underlying card
 */
public record KnownCard(Card card) implements CardView {}
