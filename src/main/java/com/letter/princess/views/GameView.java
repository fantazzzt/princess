package com.letter.princess.views;

import com.letter.princess.game.state.GameState;

import java.util.List;

/**
 * Represents a view of the game, with info filtered to requesting player (or spectator)
 * @param currentRound
 * @param numTokensToWin
 * @param currentPlayer
 * @param otherPlayers
 * @param numCardsInDeck
 */
// TODO: perhaps store game data in a metadata object instead of flat fields?
public record GameView(
        int currentRound,
        int numTokensToWin,
        PlayerView currentPlayer, // TODO: alt: make this a PlayerId, List<PlayerView> = all players including self?
        List<PlayerView> otherPlayers,
        int numCardsInDeck,
        GameState gameState
        // TODO: if requesting player == currentPlayer, return List<Action> validActions?
        // TODO: add List<Event> eventLog
) {}
