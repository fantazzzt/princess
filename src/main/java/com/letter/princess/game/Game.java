package com.letter.princess.game;

import com.letter.princess.game.state.AwaitingDraw;
import com.letter.princess.game.state.AwaitingPlay;
import com.letter.princess.game.state.GameState;
import com.letter.princess.models.*;
import com.letter.princess.views.CardView;
import com.letter.princess.views.GameView;
import com.letter.princess.views.KnownCard;
import com.letter.princess.views.PlayerView;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

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
    // TODO: add tests
    public Card drawCard() {
        if (!gameState.equals(AwaitingDraw.AWAITING_DRAW)) {
            throw new IllegalStateException("Cannot draw card");
        }
        Player currentPlayer = currentPlayer();
        if (deck.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        Card card = deck.draw();
        currentPlayer.addCardToHand(card);
        gameState = AwaitingPlay.AWAITING_PLAY;
        return card;
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Get the view of the game from the POV of given player.
     *
     * @param player Player to get view from
     * @return complete view of the game as seen by player
     */
    // TODO: add tests
    public GameView gameView(Player player) {
        return new GameView(currentRound,
                numTokensToWin,
                currentPlayerIndex,
                getPlayerViews(player.getPlayerId()),
                deck.getSize(),
                gameState);
    }

    // TODO: add tests
    private List<PlayerView> getPlayerViews(PlayerId playerId) {
        return players.stream().map(p -> getPlayerView(p, playerId)).toList();
    }

    // TODO: refactor this more, dont have isSelf boolean
    private PlayerView getPlayerView(Player player, PlayerId viewerId) {
        return new PlayerView(player.getPlayerId(),
                player.getDisplayName(),
                getHandView(player, viewerId),
                getDiscardedCardsView(player),
                player.getNumTokens(),
                player.isInRound()
        );
    }

    /**
     * Get the hand view for player from perspective of viewerId
     *
     * @param player   Player whose hand to "see"
     * @param viewerId Player viewing the hand
     * @return list of visible cards if player == viewer, otherwise list of hidden cards
     */
    private List<CardView> getHandView(Player player, PlayerId viewerId) {
        boolean revealed = player.getPlayerId().equals(viewerId);
        return revealed ? toVisibleHand(player.getHand()) : toHiddenHand(player.getHand());
    }

    /**
     * Returns a fully-visible view of the player's discarded cards, in order first to latest
     *
     * @param player Player to get discarded cards for
     * @return list of KnownCard CardViews
     */
    private List<CardView> getDiscardedCardsView(Player player) {
        return player.getDiscardedCards().stream().map(c -> (CardView) new KnownCard(c)).toList();
    }

    /**
     * Returns a fully-visible view of the player's hand
     *
     * @param hand hand to reveal
     * @return List of cards representing the hand
     */
    private List<CardView> toVisibleHand(Hand hand) {
        return hand.getCardsInHand().stream().map(CardView::known).toList();
    }

    /**
     * Returns a hidden view of the player's hand
     *
     * @param hand hand to get as hidden
     * @return List of cards representing the hand
     */
    private List<CardView> toHiddenHand(Hand hand) {
        return hand.getCardsInHand().stream().map(c -> CardView.hidden()).toList();
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

    public Player getPlayerById(UUID playerId) {
        return players.stream().filter(i -> i.getPlayerId().getGlobalId().equals(playerId)).findFirst().orElseThrow();
    }
}
