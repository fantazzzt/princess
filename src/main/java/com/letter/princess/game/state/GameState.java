package com.letter.princess.game.state;

public sealed interface GameState permits
        AwaitingDraw,
        AwaitingPlay,
        AwaitingEffectResolution,
        AwaitingEndTurn,
        GameOver {
}
