package com.letter.princess.models;

/**
 * Represents the rank of a card
 */
public enum Role {
    SPY(0),
    GUARD(1),
    PRIEST(2),
    BARON(3),
    HANDMAID(4),
    PRINCE(5),
    CHANCELLOR(6),
    KING(7),
    COUNTESS(8),
    PRINCESS(9);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
