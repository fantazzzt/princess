package com.letter.princess.views;

import java.util.List;

/**
 * Represents a view of the game, with info filtered to requesting player (or spectator)
 * @param currentRound
 * @param numTokensToWin
 * @param currentPlayer
 * @param otherPlayers
 * @param numCardsInDeck
 */
public record GameView(
        int currentRound,
        int numTokensToWin,
        PlayerView currentPlayer,
        List<PlayerView> otherPlayers,
        int numCardsInDeck
        // TODO: how to model current state?
        // TODO: perhaps store game data in a metadata object instead of flat fields?
) {}
