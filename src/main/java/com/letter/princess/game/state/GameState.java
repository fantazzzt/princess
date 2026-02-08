package com.letter.princess.game.state;

public sealed interface GameState permits AwaitingDraw,
        AwaitingPlay,
        AwaitingTarget,
        AwaitingEffectResolution,
        AwaitingEndTurn,
        GameOver {
}
