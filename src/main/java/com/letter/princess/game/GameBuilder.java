package com.letter.princess.game;

import com.letter.princess.game.state.AwaitingDraw;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import com.letter.princess.models.PlayerId;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // TODO: when adding player, maybe need their id and playerName, not just
    //  name (so we can support multiple players with same name)
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
        Deck newDeck = Deck.newDeck();
        List<Player> players = new ArrayList<>();
        for (String name : playerNames) {
            PlayerId playerId = new PlayerId(name); // TODO: use UUID
            Player newPlayer = new Player(playerId, name);
            newPlayer.addCardToHand(newDeck.draw());
            players.add(newPlayer);
        }
        Card discardedCard = newDeck.draw();
        // TODO: player order random?
        return new Game(players, 0, NUM_TOKENS_WIN, newDeck,
                Collections.singletonList(discardedCard),
                1, AwaitingDraw.AWAITING_DRAW);
    }
}
