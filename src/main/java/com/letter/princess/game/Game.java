package com.letter.princess.game;

import com.letter.princess.game.logic.CardLogic;
import com.letter.princess.game.logic.CardLogicLookup;
import com.letter.princess.game.state.AwaitingDraw;
import com.letter.princess.game.state.AwaitingPlay;
import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Card;
import com.letter.princess.models.CardData;
import com.letter.princess.models.Deck;
import com.letter.princess.models.PlayCardResult;
import com.letter.princess.models.Player;
import com.letter.princess.models.PlayerId;
import com.letter.princess.views.CardView;
import com.letter.princess.views.GameView;
import com.letter.princess.views.KnownCard;
import com.letter.princess.views.PlayerView;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
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
    public PlayCardResult playCard(Player player, CardData cardData) {
        // 1. validate
        /* TODO
        1. check player is current player
        2. check state is valid (awaitPlay)
        3. check card is valid to play
            1. card is in player's hand
            2. no special rule (ex. Countess)
         */
        // 2. discard card
        // 3. apply card effect
        CardLogic logic = CardLogicLookup.getCardLogic(cardData.getCard().role());
        Player target = null;
        if (logic.hasTarget()) {
            List<Player> validTargets = getValidTargets(player, logic);
            if (validTargets.isEmpty() && cardData.getTarget() != null) {
                throw new IllegalArgumentException("There are no valid targets, but a target was given");
            }
            Optional<Player> opt = validTargets.stream().filter(
                    p -> p.getDisplayName().equals(cardData.getTarget())).findFirst();
            if (opt.isEmpty()) {
                throw new IllegalArgumentException("There is a valid target but none provided");
            }
            target = opt.get();
        }
        PlayCardResult result = logic.applyCardEffect(this, cardData, player, target);
        // 4. add to event log
        // 5. return response
        return result;
    }

    private List<Player> getValidTargets(Player player, CardLogic cardLogic) {
        return players.stream().filter(p -> {
            if (p.equals(player)) {
                return cardLogic.isSelfValidTarget();
            }
            return p.isInRound() && !p.isImmune();
        }).toList();
    }

    // TODO: fix Hand abstraction, it's not working
    public void eliminatePlayer(Player player) {
        player.setInRound(false);
        List<Card> cardsInHand = player.getHand().getCardsInHand();
        player.getDiscardedCards().addAll(cardsInHand);
        cardsInHand.clear();
        // TODO: if that was the last player in the game, do end-of-round sequence
    }

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

    // TODO: refactor PlayerId stuff to make it more straightforward
    public Player getPlayerById(UUID playerId) {
        return players.stream().filter(i -> i.getPlayerId().getGlobalId().equals(playerId)).findFirst().orElseThrow();
    }
}
