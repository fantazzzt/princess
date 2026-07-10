package com.letter.princess.game.logic;

import com.letter.princess.models.Card;
import org.junit.jupiter.api.Test;

import static com.letter.princess.models.Role.PRIEST;
import static com.letter.princess.models.Role.SPY;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardLogicLookupTest {

    @Test
    public void getLogicForCard_returnsSpyLogicForSpy() {
        assertInstanceOf(SpyLogic.class,
                CardLogicLookup.getLogicForCard(new Card(SPY)));
    }

    @Test
    public void getLogicForCard_throwsForCardWithoutLogic() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> CardLogicLookup.getLogicForCard(new Card(PRIEST)));
        assertTrue(ex.getMessage().contains("Unknown card"),
                "expected message containing 'Unknown card' but got: "
                        + ex.getMessage());
    }
}
