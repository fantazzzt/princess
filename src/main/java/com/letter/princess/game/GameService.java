package com.letter.princess.game;

import org.springframework.stereotype.Service;

import static com.letter.princess.game.state.GameOver.GAME_OVER;

@Service
public class GameService {

    private Game currentGame;
    private GameBuilder gameBuilder;

    public void newLobby() {
        if (currentGame != null && !GAME_OVER.equals(currentGame.getGameState())) {
            throw new IllegalArgumentException("Cannot start new game while " +
                    "current game is ongoing");
        }
        currentGame = null;
        gameBuilder = GameBuilder.initializeGame();
    }

    public void addPlayer(String player) {
        if (gameBuilder == null) {
            throw new IllegalArgumentException("No game lobby active");
        }
        gameBuilder.addPlayer(player);
    }

    public void removePlayer(String player) {
        if (gameBuilder == null) {
            throw new IllegalArgumentException("No game lobby active");
        }
        gameBuilder.removePlayer(player);
    }

    // TODO: race condition here, can have lobby and current game started
    public void startGame() {
        this.currentGame = gameBuilder.startGame();
        this.gameBuilder = null;
    }

    public Game getGame() {
        if (currentGame == null) {
            throw new IllegalStateException("Game not started");
        }
        return currentGame;
    }
}