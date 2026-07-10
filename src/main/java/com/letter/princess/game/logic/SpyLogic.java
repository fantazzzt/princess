package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
import com.letter.princess.models.GameAction;
import com.letter.princess.models.Player;
import com.letter.princess.models.Role;

public class SpyLogic implements CardLogic {

    @Override
    public boolean isValidPlayCardAction(Game game, Player player, GameAction action) {
        if (action.card().role() != Role.SPY) {
            return false; // card doesn't match this logic
        }
        if (action.target() != null || action.guess() != null) {
            return false; // these fields should be empty
        }
        return true;
    }

    @Override
    public boolean apply(Game game, Player player, GameAction action) {
        player.setPlayedSpyThisRound(true);
        return true;
    }
}
