package com.letter.princess.models;

import lombok.Builder;

import java.util.List;

/**
 * Used to respond to client the result of playing a card
 */
@Builder
public class PlayCardResult {
    // for Priest
    private final Card seenCard;
    // for Chancellor
    private final List<Card> drawnCards;
}
