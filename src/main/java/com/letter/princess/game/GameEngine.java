package com.letter.princess.game;

import com.letter.princess.game.state.AwaitingPlay;
import com.letter.princess.models.GameAction;
import com.letter.princess.models.PlayCardResult;
import com.letter.princess.models.Player;
import lombok.NonNull;

import static com.letter.princess.game.state.AwaitingPlay.AWAITING_PLAY;
import static com.letter.princess.models.Action.PLAY_CARD;

/**
 * Executes game actions, including validation and applying card rules
 */
public class GameEngine {

    /**
     * Validate that the given GameAction to play a card is valid.
     * Assumes that player is already checked to be in the game, otherwise we
     * couldn't get here.
     * @param game Game to try and play card in
     * @param player Player in the game who wants to play card
     * @param action Request from client to play card
     * @return
     */
    public void validatePlayCard(@NonNull Game game,
                                    @NonNull Player player,
                                    @NonNull GameAction action) {
        if (!AWAITING_PLAY.equals(game.getGameState())) {
            throw new IllegalArgumentException("Game state should be " +
                    "AwaitingPlay, is " + game.getGameState());
        }
        if (!game.isCurrentPlayer(player)) {
            throw new IllegalArgumentException("It is not this player's turn");
        }
        if (!PLAY_CARD.equals(action.action())) {
            throw new IllegalArgumentException("Player action must be to play" +
                    " card");
        }
        // TODO: check that card is valid to play
    }

    public PlayCardResult playCard(Game game, Player player,
                                   GameAction action) {
        return null;
    }
}
