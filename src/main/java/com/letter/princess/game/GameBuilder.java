package com.letter.princess.game;

import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.letter.princess.game.state.Initializing.INITIALIZING;

public class GameBuilder {
    // TODO: add support for mapping num players to num tokens to win
    private static final int NUM_TOKENS_WIN = 5;
    private static final int MIN_NUM_PLAYERS = 2;
    private static final int MAX_NUM_PLAYERS = 6;

    private final List<String> playerNames;

    private GameBuilder() {
        this.playerNames = new ArrayList<>();
    }

    public static GameBuilder initializeGame() {
        return new GameBuilder();
    }

    public boolean isGameFull() {
        return playerNames.size() >= MAX_NUM_PLAYERS;
    }

    // DESIGN DECISION: players identified by playerName only, no id yet
    public GameBuilder addPlayer(@NonNull String playerName) {
        if (playerNames.contains(playerName)) {
            throw new IllegalArgumentException("Cannot have 2 players with " +
                    "same name " + playerName);
        }
        if (isGameFull()) {
            throw new IllegalStateException("Cannot have more than " + MAX_NUM_PLAYERS + " players in game");
        }
        playerNames.add(playerName);
        return this;
    }

    public GameBuilder removePlayer(@NonNull String playerName) {
        playerNames.remove(playerName);
        return this;
    }

    public List<String> getPlayerNames() {
        return new ArrayList<>(playerNames);
    }

    /**
     * Game factory
     *
     * @return newly-initialised game of Princess
     */
    public Game startGame() {
        if (playerNames.size() < MIN_NUM_PLAYERS) {
            throw new IllegalStateException("Not enough players in the game! Need at least " + MIN_NUM_PLAYERS + "players");
        }
        // 1. new deck
        Deck newDeck = Deck.testDeck();
        List<Player> players = new ArrayList<>();
        // 2. player list
        for (String name : playerNames) {
            Player newPlayer = new Player(name);
            players.add(newPlayer);
        }
        // TODO: shuffle player list
        Game newGame = new Game(1, 0, NUM_TOKENS_WIN,
                players, newDeck, new ArrayList<>(), INITIALIZING);
        // 3. init game: removed cards, draw to each player's hand
        newGame.init();
        return newGame;
    }
}
