package com.letter.princess.game;

import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import com.letter.princess.views.*;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Represents a complete game of Princess
 */
@AllArgsConstructor
public class Game {

    private final List<Player> players;
    private int currentPlayerIndex;
    private final int numTokensToWin;
    private final Deck deck;
    private final List<Card> discardedCards;
    private int currentRound;
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
                currentPlayerIndex,
                getPlayerViews(player),
                deck.getSize(),
                gameState);
    }

    // TODO: add tests
    private List<PlayerView> getPlayerViews(Player player) {
        return players.stream().map(p -> getPlayerView(p, p.equals(player))).toList();
    }

    // TODO: refactor this more, dont have isSelf boolean
    private PlayerView getPlayerView(Player player, boolean isSelf) {
        return new PlayerView(player.getDisplayName(),
                isSelf ? getVisibleHand(player) : getHiddenHand(player),
                getDiscardedCardsView(player),
                player.getNumTokens(),
                player.isInRound()
        );
    }

    /**
     * Returns a fully-visible view of the player's discarded cards, in order first to latest
     * @param player Player to get discarded cards for
     * @return list of KnownCard CardViews
     */
    private List<CardView> getDiscardedCardsView(Player player) {
        return player.getDiscardedCards().stream().map(c -> (CardView) new KnownCard(c)).toList();
    }

    /**
     * Returns a fully-visible view of the player's hand
     * @param player Player whose hand view to return
     * @return List of cards representing the hand
     */
    private List<CardView> getVisibleHand(Player player) {
        return player.getHand().getCardsInHand().stream().map(c -> (CardView) new KnownCard(c)).toList();
    }

    private List<CardView> getHiddenHand(Player player) {
        return player.getHand().getCardsInHand().stream().map(c -> (CardView) HiddenCard.HIDDEN_CARD).toList();
    }

    public int currentRound() {
        return currentRound;
    }

    public int numTokensToWin() {
        return numTokensToWin;
    }

    public List<Player> players() {
        return List.copyOf(players);
    }
}
