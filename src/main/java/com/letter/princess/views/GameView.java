package com.letter.princess.views;

import java.util.List;

public record GameView(
        int currentRound,
        int numTokensToWin,
        PlayerView currentPlayer,
        List<PlayerView> otherPlayers
) {}
