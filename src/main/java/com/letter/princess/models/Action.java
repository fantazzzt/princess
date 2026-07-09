package com.letter.princess.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Action {
    DRAW_CARD("drawCard"),
    PLAY_CARD("playCard");

    private final String actionName;
}
