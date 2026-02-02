package com.letter.princess.views;

import java.util.List;

/**
 * Represents a view of a player, sent to client
 * @param playerName Name of player TODO: switch to PlayerId
 * @param hand Player's current hand, contains CardViews (can be known or hidden cards depending on POV)
 * @param discardedCards player's discarded cards in order (earliest to latest)
 * @param numTokens player's num tokens
 * @param isInRound whether player is still in the round. could also infer this from hand being empty, but
 *                  prefer avoid client side inferring
 */
public record PlayerView(
        String playerName,
        List<CardView> hand,
        List<CardView> discardedCards,
        int numTokens,
        boolean isInRound
) {}
