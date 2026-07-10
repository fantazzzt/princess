package com.letter.princess.game.logic;

import com.letter.princess.game.state.GameState;
import com.letter.princess.models.Action;
import com.letter.princess.models.Card;
import com.letter.princess.models.Role;

import java.util.HashMap;
import java.util.Map;

public class CardLogicLookup {

    private static final Map<Role, CardLogic> cardLogicLookup =
            buildLookupMap();

    private static Map<Role, CardLogic> buildLookupMap() {
        Map<Role, CardLogic> map = new HashMap<>();
        map.put(Role.SPY, new SpyLogic());
        return map;
    }

    public static CardLogic getLogicForCard(Card card) {
        CardLogic cardLogic = cardLogicLookup.get(card.role());
        if (cardLogic == null) {
            throw new IllegalStateException("Unknown card: " + card);
        }
        return cardLogic;
    }
}
