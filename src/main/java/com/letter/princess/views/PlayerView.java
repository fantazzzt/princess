package com.letter.princess.views;

import java.util.List;

/**
 * Represents a view of a player, sent to client
 * @param playerName
 * @param hand
 * @param discardedCards
 * @param numTokens
 */
public record PlayerView(
        String playerName,
        List<CardView> hand,
        List<CardView> discardedCards,
        int numTokens,
        boolean isInRound // could also infer this from hand being empty, but prefer avoid client side inferring
) {}
