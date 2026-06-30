package com.letter.princess.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Action {
    PLAY_CARD("playCard");

    private final String actionName;
}
