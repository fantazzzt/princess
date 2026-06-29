package com.letter.princess.game.state;

public sealed interface GameState permits
        Initializing,
        AwaitingDraw,
        AwaitingPlay,
        AwaitingEffectResolution,
        AwaitingEndTurn,
        GameOver {
}
