package com.letter.princess.game;

import com.letter.princess.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameBuilderTest {

    private static final int MAX_NUM_PLAYERS = 6;
    private static final int NUM_TOKENS_WIN = 5;

    private GameBuilder gameBuilder;

    @BeforeEach
    public void setup() {
        gameBuilder = GameBuilder.initializeGame();
    }

    @Test
    public void testAddPlayer() {
        gameBuilder.addPlayer("olga");
        assertEquals(List.of("olga"), gameBuilder.getPlayerNames());
    }

    @Test
    public void testRemovePlayer() {
        gameBuilder.addPlayer("olga")
                .addPlayer("caden")
                .addPlayer("masha")
                .addPlayer("polina");
        gameBuilder.removePlayer("olga")
                .removePlayer("polina");
        assertEquals(List.of("caden", "masha"), gameBuilder.getPlayerNames());
    }

    @Test
    public void testRemovePlayer_emptyOrNonexistentPlayer() {
        assertTrue(gameBuilder.getPlayerNames().isEmpty());
        gameBuilder.removePlayer("caden");
        assertTrue(gameBuilder.getPlayerNames().isEmpty());
        gameBuilder.addPlayer("olga");
        gameBuilder.removePlayer("caden");
        assertEquals(List.of("olga"), gameBuilder.getPlayerNames());
    }

    @Test
    public void testAddPlayer_gameFull() {
        for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
            gameBuilder.addPlayer("player" + i);
        }
        assertThrows(IllegalStateException.class, () ->
                gameBuilder.addPlayer("extra"));
    }

    @Test
    public void testStartGame() {
        Game game = gameBuilder.addPlayer("olga")
                .addPlayer("caden")
                .startGame();
        assertEquals(1, game.getCurrentRound());
        assertEquals(NUM_TOKENS_WIN, game.getNumTokensToWin());
        List<Player> players = game.getPlayers();
        assertEquals(2, players.size());
        assertEquals("olga", players.get(0).getDisplayName());
        assertEquals("caden", players.get(1).getDisplayName());
    }

    @Test
    public void testStartGame_tooFewPlayers() {
        assertThrows(IllegalStateException.class, () ->
                gameBuilder.startGame());
    }

}
