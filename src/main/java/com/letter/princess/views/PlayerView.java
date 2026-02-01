package com.letter.princess.views;

import com.letter.princess.models.Card;

import java.util.List;

public record PlayerView(
        String playerName,
        List<CardView> hand,
        List<CardView> discardedCards,
        int numTokens
) {}
