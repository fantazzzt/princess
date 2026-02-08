package com.letter.princess.views;

import com.letter.princess.game.state.GameState;

import java.util.List;

/**
 * Represents a view of the game, with info filtered to requesting player (or spectator)
 * @param currentRound
 * @param numTokensToWin
 * @param currentPlayerIndex Index of current player in list of players
 * @param players
 * @param numCardsInDeck
 * @param gameState What state the game is in (e.g AwaitingDraw)
 */
// TODO: perhaps store game data in a metadata object instead of flat fields?
public record GameView(
        int currentRound,
        int numTokensToWin,
        int currentPlayerIndex,
        List<PlayerView> players,
        int numCardsInDeck,
        GameState gameState
        // TODO: if requesting player == currentPlayer, return List<Action> validActions?
        // TODO: add List<Event> eventLog
) {}
