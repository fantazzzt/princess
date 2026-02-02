package com.letter.princess.game.state;

sealed public interface GameState permits AwaitingDraw,
        AwaitingPlay,
        AwaitingTarget,
        AwaitingEffectResolution,
        AwaitingEndTurn {}