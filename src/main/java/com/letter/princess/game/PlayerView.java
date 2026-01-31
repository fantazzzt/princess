package com.letter.princess.game;

import com.letter.princess.models.Card;

import java.util.List;

public record PlayerView(
        String playerName,
        int numCards,
        List<Card> hand,
        List<Card> discardedCards,
        int numTokens
) {}
