package com.letter.princess.game;

import com.letter.princess.game.logic.CardLogic;
import com.letter.princess.game.logic.CardLogicLookup;
import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Action;
import com.letter.princess.models.Card;
import com.letter.princess.models.GameAction;
import com.letter.princess.models.PlayCardResult;
import com.letter.princess.models.Player;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.letter.princess.game.state.AwaitingDraw.AWAITING_DRAW;
import static com.letter.princess.game.state.AwaitingEndTurn.AWAITING_END_TURN;
import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.models.Action.DRAW_CARD;
import static com.letter.princess.models.Action.PLAY_CARD;
import static com.letter.princess.models.Role.PRINCESS;

/**
 * Executes game actions, including validation and applying card rules
 */
public class GameEngine {

    private static final Map<GameState, Action> validActionMap =
            buildValidActionsMap();

    // TODO: should this go into separate Validator class?
    private static Map<GameState, Action> buildValidActionsMap() {
        Map<GameState, Action> map = new HashMap<>();
        map.put(AWAITING_DRAW, DRAW_CARD);
        map.put(AWAITING_PLAY, PLAY_CARD);
        return map;
    }

    /**
     * Validate game action against current game state
     */
    public boolean validateAction(@NonNull final Game game,
                               @NonNull final GameAction gameAction) {
        if (gameAction.action() == null) {
            return false;
        }
        return gameAction.action().equals(validActionMap.get(game.getGameState()));
    }

    /**
     * Perform player action of drawing a card
     * Precondition: game state is AWAITING_DRAW, it is player's turn
     * this method should NOT be used to draw a card for non-current player
     * TODO: above seems fragile; how to enforce this/make it easy to do
     *  right thing, hard to do wrong thing?
     * @param player Current player (must be validated before this method)
     * @return Card that was drawn
     * @throws IllegalStateException if game state is not Adeck has no cards
     */
    public Card drawCard(@NonNull final Game game, @NonNull final Player player) {
        if (game.isDeckEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        Card card = game.drawCard(player);
        game.setGameState(AWAITING_PLAY);
        return card;
    }

    /**
     * Validate that the given GameAction to play a card is valid.
     * Precondition: player is already checked to be in the game
     * @param game Game to try and play card in
     * @param player Player in the game who wants to play card
     * @param action Request from client to play card
     */
    public void validatePlayCard(@NonNull Game game,
                                    @NonNull Player player,
                                    @NonNull GameAction action) {
        // TODO: should this be moved up a level?
        if (!game.isCurrentPlayer(player)) {
            throw new IllegalArgumentException("It is not this player's turn");
        }
        if (!validateAction(game, action)) {
            throw new IllegalArgumentException("Game state should be " +
                    "AwaitingPlay, is " + game.getGameState());
        }
        if (!PLAY_CARD.equals(action.action())) {
            throw new IllegalArgumentException("Player action must be to play" +
                    " card");
        }

        Card cardToPlay = action.card();
        if (!player.getHand().contains(cardToPlay)) {
            throw new IllegalArgumentException("Player's hand doesn't contain" +
                    " this card");
        }

        // Check card-specific rules
        CardLogic cardLogic = CardLogicLookup.getLogicForCard(cardToPlay);
        if (!cardLogic.isValidPlayCardAction(game, player, action)) {
            throw new IllegalArgumentException("Card is not valid to play");
        }
    }

    /**
     * Execute the play card action
     */
    public void playCard(Game game, Player player,
                                   GameAction action) {
        Card card = action.card();
        CardLogic cardLogic = CardLogicLookup.getLogicForCard(card);
        // 0. (later) TODO: add to EventLog
        // 1. discard card - TODO: check if player out
        boolean isPlayerInGame = discardCard(game, player, card);
        // 2. apply card rule
        if (isPlayerInGame) {
            cardLogic.apply(game, player, action);
            // 4. TODO check for pending effects (Chancellor)
            // 5. TODO check if round/game is over
            game.setGameState(AWAITING_END_TURN);
        }
        // TODO: else, handle player exit from game
    }

    /**
     * Discard card from the player's hand and check for Princess (todo)
     * @return true if player still in game
     */
    private boolean discardCard(Game game, Player player, Card card) {
        if (!player.getHand().contains(card)) {
            throw new IllegalStateException("Attempting to discard card not " +
                    "in hand");
        }
        game.discardCard(player, card);
        // TODO: check for princess and handle
        return true;
    }
}
