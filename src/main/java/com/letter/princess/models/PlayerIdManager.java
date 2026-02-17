package com.letter.princess.models;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerIdManager {
    private static final Map<String, Integer> playerNameToId = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(0);

    public static UUID getGlobalId(String playerName) {
        return UUID.nameUUIDFromBytes(playerName.getBytes());
    }

    public static int getInternalId(String playerName) {
        playerNameToId.computeIfAbsent(playerName, k -> nextId.addAndGet(1));
        return playerNameToId.get(playerName);
    }

    private PlayerIdManager() {
    }
}
