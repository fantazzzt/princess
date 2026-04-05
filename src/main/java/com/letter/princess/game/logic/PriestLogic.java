package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
import com.letter.princess.models.*;
import lombok.NonNull;

public class PriestLogic implements CardLogic {
    @Override
    public Role role() {
        return Role.PRIEST;
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
    public PlayCardResult applyCardEffect(@NonNull Game game,
                                          @NonNull CardData cardData,
                                          @NonNull Player currentPlayer,
                                          Player targetPlayer) {
        // TODO: refactor, this is so unwieldy
        Card seenCard = targetPlayer.getHand().getCardsInHand().getFirst();
        return PlayCardResult.builder()
                .seenCard(seenCard)
                .build();
    }
}
