package com.letter.princess.models;

public class Card implements Comparable<Card> {
    private final Rank rank;

    public Card(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return this.rank.toString();
    }

    @Override
    public int compareTo(Card other) {
        return this.rank.compareTo(other.rank);
    }


    public enum Rank {
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

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
