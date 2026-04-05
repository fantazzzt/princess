package com.letter.princess.game.logic;

import com.letter.princess.models.Role;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardLogicLookup {

    private static final Map<Role, CardLogic> LOOKUP = Stream.of(
            new SpyLogic(),
            new GuardLogic(),
            new PriestLogic()
    ).collect(Collectors.toUnmodifiableMap(CardLogic::role, c -> c));

    public static CardLogic getCardLogic(Role role) {
        return LOOKUP.get(role);
    }
}
