package com.letter.princess.models;

import lombok.Getter;

import java.util.UUID;

public class PlayerId {
    @Getter
    private int internalId;
    @Getter
    private UUID globalId;

    public PlayerId(String playerName) {
        this.globalId = (PlayerIdManager.getGlobalId(playerName));
        this.internalId = (PlayerIdManager.getInternalId(playerName));
    }
}
