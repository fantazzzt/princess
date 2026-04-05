package com.letter.princess.game.logic;

import com.letter.princess.game.Game;
import com.letter.princess.models.CardData;
import com.letter.princess.models.PlayCardResult;
import com.letter.princess.models.Player;
import com.letter.princess.models.Role;

/**
 * Attempt 1 at implementing card-specific logic.
 * Each card implements this interface.
 *
 * QUESTION: should CardLogic directly implement isValidMove? The problem is
 * some rules of validity are card-agnostic and belong in Game (e.g. is the
 * card currently in the player's hand), and some are card-specific
 * (e.g. if there's a target, if there's a guess, if currentPlayer has King and
 * Countess in hand). This version of interface provides the tools to know
 * whether card has a target and stuff, but guess stuff is still implemented
 * in GuardLogic... not sure this is the right abstraction though.
 */
public interface CardLogic {

    /**
     * For building CardLogicLookup of role -> CardLogic
     */
    Role role();

    /**
     * For validating "playCard" action, returns true if card has a target
     */
    boolean hasTarget();

    /**
     * For validating "playCard" action, return true if self can be a target
     */
    boolean isSelfValidTarget();

    /**
     * Apply effect of the card, assuming CardDate has been validated
     * @param game Game where card is being played, will modify game
     * @param cardData Validated card data: role, target, guess (if applicable)
     * @param currentPlayer who's playing the card
     * @param targetPlayer target (may be null)
     * @return Result of playing the card
     */
    PlayCardResult applyCardEffect(Game game, CardData cardData,
                                   Player currentPlayer, Player targetPlayer);
}