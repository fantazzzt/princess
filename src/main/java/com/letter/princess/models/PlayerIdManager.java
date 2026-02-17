package com.letter.princess.models;

import lombok.Singular;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerIdManager {
    @Singular
    static Map<String, Integer> playerNameToId = new LinkedHashMap<>();
    static int nextId = 0;

    public static UUID getGlobalId(String playerName) {
        return UUID.nameUUIDFromBytes(playerName.getBytes());
    }

    public static int getInternalId(String playerName) {
        playerNameToId.computeIfAbsent(playerName, k -> nextId++);
        return playerNameToId.get(playerName);
    }

    private PlayerIdManager() {
    }
}
