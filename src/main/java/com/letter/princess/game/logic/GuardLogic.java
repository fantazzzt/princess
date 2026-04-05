package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
import com.letter.princess.models.*;
import lombok.NonNull;

import static com.letter.princess.models.Role.GUARD;

public class GuardLogic implements CardLogic {
    @Override
    public Role role() {
        return GUARD;
    }

    @Override
    public boolean hasTarget() {
        return true;
    }

    @Override
    public boolean isSelfValidTarget() {
        return false;
    }

    @Override
    public PlayCardResult applyCardEffect(@NonNull Game game, CardData cardData,
                                          Player currentPlayer,
                                          Player targetPlayer) {
        Card guess = cardData.getGuardGuess();
        if (targetPlayer == null || guess == null || GUARD.equals(guess.role())) {
            throw new IllegalArgumentException("Must guess a non-Guard card");
        }
        Hand targetPlayerHand = targetPlayer.getHand();
        if (targetPlayerHand.getCardsInHand().contains(guess)) {
            game.eliminatePlayer(targetPlayer);
        }
        return PlayCardResult.builder().build();
    }
}
