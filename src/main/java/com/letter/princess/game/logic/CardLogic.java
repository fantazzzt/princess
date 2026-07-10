package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
import com.letter.princess.models.GameAction;
import com.letter.princess.models.Player;

public interface CardLogic {

    /**
     * Check if player can play the current card based on game rules
     * Assumes card is in player's hand
     */
    boolean isValidPlayCardAction(Game game, Player player, GameAction action);

    /**
     * Apply given card's rules in the game
     * Assumes player can play this card
     * @return true if successful (later return EventLog I guess idk)
     */
    boolean apply(Game game, Player player, GameAction action);
}
