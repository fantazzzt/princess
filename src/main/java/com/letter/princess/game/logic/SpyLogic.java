package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
import com.letter.princess.models.CardData;
import com.letter.princess.models.PlayCardResult;
import com.letter.princess.models.Player;
import com.letter.princess.models.Role;

public class SpyLogic implements CardLogic {
    @Override
    public Role role() {
        return Role.SPY;
    }

    @Override
    public boolean hasTarget() {
        return false;
    }

    @Override
    public boolean isSelfValidTarget() {
        return false;
    }

    @Override
    public PlayCardResult applyCardEffect(Game game, CardData cardData,
                                          Player currentPlayer,
                                          Player targetPlayer) {
        currentPlayer.setHasPlayedSpyThisRound(true);
        return PlayCardResult.builder().build();
    }
}
