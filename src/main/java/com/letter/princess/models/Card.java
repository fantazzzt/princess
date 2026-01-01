package com.letter.princess.models;

import lombok.Getter;

/**
 * Represents a Card that can be played.
 * Cards have a role and can be compared to other cards.
 * Cards are immutable once created.
 */
@Getter
public class Card implements Comparable<Card> {
    private final Role role;

    public Card(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role.toString();
    }

    @Override
    public int compareTo(Card other) {
        return this.role.compareTo(other.role);
    }

}
