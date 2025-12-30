package com.letter.princess.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    public void testToString() {
        final Card princess = new Card(Role.PRINCESS);
        assertEquals("PRINCESS", princess.toString());
    }

    @Test
    public void testRole() {
        final Card princess = new Card(Role.PRINCESS);
        assertEquals(Role.PRINCESS, princess.getRole());
    }
}
