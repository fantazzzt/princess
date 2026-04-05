package com.letter.princess.models;

import lombok.Builder;
import lombok.Getter;

/**
 * Used in request to play a card, sent from client to server
 */
@Getter
@Builder
public class CardData {
    private final Card card;
    private final String target;
    private final Card guardGuess;
}
