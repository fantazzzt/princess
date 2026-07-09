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
        // TODO: build the real deck and shuffle it (and player order) here.
        return startGameWithDeck(Deck.testDeck());
    }

    /**
     * Build and initialise a game over a caller-supplied deck, used in its
     * given order (no shuffling).
     * @param deck deck to play with, used in its given order
     * @return newly-initialised game of Princess
     */
    Game startGameWithDeck(@NonNull Deck deck) {
        if (playerNames.size() < MIN_NUM_PLAYERS) {
            throw new IllegalStateException("Not enough players in the game! " +
                    "Need at least " + MIN_NUM_PLAYERS + " players");
        }
        List<Player> players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));
        }
        // TODO: shuffle player list (production only; tests rely on lobby order)
        Game newGame = new Game(1, 0, NUM_TOKENS_WIN, players, deck,
                new ArrayList<>(), INITIALIZING);
        newGame.init();
        return newGame;
    }
}
