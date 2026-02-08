package com.letter.princess.models;

/**
 * Represents a Card that can be played.
 * Cards have a role and can be compared to other cards.
 * Cards are immutable once created.
 */
public record Card(Role role) implements Comparable<Card> {

    @Override
    public String toString() {
        return this.role.toString();
    }

    @Override
    public int compareTo(Card other) {
        return this.role.compareTo(other.role);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card other)) {
            return false;
        }
        return this.role.equals(other.role);
    }
}
