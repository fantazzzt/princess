package com.letter.princess.game;

import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import com.letter.princess.views.CardView;
import com.letter.princess.views.GameView;
import com.letter.princess.views.KnownCard;
import com.letter.princess.views.PlayerView;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Represents a complete game of Princess
 */
@AllArgsConstructor
public class Game {

    @Getter // TODO: probably shouldn't be a getter, need a defensive copy
    private final List<Player> players;
    @Getter
    private final int numTokensToWin;
    private final Deck deck;
    private final List<Card> discardedCards;
    @Getter
    private int currentRound;
    @Getter
    private GameState gameState;

    // TODO: implement actions
    /*
     PLAYING CARDS
     drawCard(player) -> draw card from deck
     getValidCardsToPlay(player) -> shows what cards can be chosen (e.g. if
         King and Countess in hand, then only Countess)
     playCard(player) -> take card out of hand, apply rules
     getValidTargets(player, card) -> get list of players that can be
         selected as targets

     RESOLVING EFFECTS

     EVENTS
     addEvent(GameEvent)
     ...
     */
    /**
     * Get the view of the game from the POV of given player.
     * @param player Player to get view from
     * @return
     */
    // TODO: fix how it gets currentPlayer and otherPlayer views
    // TODO: add tests
    // TODO: how to get spectator's game view?
    public GameView gameView(Player player) {
        return new GameView(currentRound,
                numTokensToWin,
                getSelfView(player),
                Collections.emptyList(), // TODO: get other player views
                deck.getSize(),
                gameState);
    }

    // TODO: consider making this getView(Player player, boolean isSelf)
    // TODO: add tests
    private PlayerView getSelfView(Player player) {
        return new PlayerView(player.getName(),
                getHandView(player),
                Collections.emptyList(),
                player.getNumTokens(),
                player.isInRound()
                );
    }

    /**
     * Returns a fully-visible view of the player's hand
     * @param player
     * @return
     */
    private List<CardView> getHandView(Player player) {
        return player.getHand().getCardsInHand().stream().map(c -> (CardView) new KnownCard(c)).toList();
    }
}
