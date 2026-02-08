package com.letter.princess.game;

import com.letter.princess.game.state.AwaitingDraw;
import com.letter.princess.models.*;
import com.letter.princess.views.GameView;
import com.letter.princess.views.KnownCard;
import com.letter.princess.views.PlayerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameTest {

    private Game game;
    @Mock
    private Player mockPlayer;
    @Mock
    private Deck mockDeck;

    @BeforeEach
    public void setUp() {
        game = new Game(
                List.of(mockPlayer),
                0,
                1,
                mockDeck,
                Collections.emptyList(),
                1,
                AwaitingDraw.AWAITING_DRAW
        );
    }

    // TODO: use real stuff instead of mocks
    @Test
    public void testGameView() {
        Hand mockHand = Mockito.mock(Hand.class);
        PlayerId mockPlayerId = new PlayerId("abc");
        Card card = new Card(Role.PRIEST);
        PlayerView expectedPlayerView = new PlayerView(
                mockPlayerId,
                "Alice",
                List.of(new KnownCard(card)),
                List.of(),
                0,
                true
        );

        when(mockPlayer.getPlayerId()).thenReturn(mockPlayerId);
        when(mockPlayer.getDisplayName()).thenReturn("Alice");
        when(mockPlayer.getHand()).thenReturn(mockHand);
        when(mockHand.getCardsInHand()).thenReturn(List.of(card));
        when(mockPlayer.getDiscardedCards()).thenReturn(Collections.emptyList());
        when(mockPlayer.getNumTokens()).thenReturn(0);
        when(mockPlayer.isInRound()).thenReturn(true);
        when(mockDeck.getSize()).thenReturn(5);
        GameView expectedGameView = new GameView(
                1,
                1,
                0,
                List.of(expectedPlayerView),
                5,
                AwaitingDraw.AWAITING_DRAW
        );
        GameView gameView = game.gameView(mockPlayer);
        assertEquals(expectedGameView, gameView);
    }
}
