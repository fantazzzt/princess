package com.letter.princess.game;

import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete game of Princess
 */
@AllArgsConstructor
public class Game {

    private final List<Player> players;
    private final int numTokensToWin;
    private final Deck deck;
    private final Card discardedCard;
    // TODO: add support for 2-player game (3 discarded cards, faceup)
    private int currentRound;

    // TODO: implement actions
    /*
     getGameState(player) -> shows current state of the world from POV of player
         do we need a field to select current player? then getGameState doesnt
         need to take player as arg
     drawCard(player) -> draw card from deck
     getValidCardsToPlay(player) -> shows what cards can be chosen (e.g. if
         King and Countess in hand, then only Countess)
     playCard(player) -> take card out of hand, apply rules
     getValidTargets(player, card) -> get list of players that can be
         selected as targets
     ...
     */

    /*
    // TODO: where does this loop go?
     while (!gameOver) -> check if anyone reached numTokensToWin
     while (!roundOver) -> check if deck is empty or num players remaining = 1
     move to next player
     draw card from deck
     player picks card
     implement card rules
     check if any players are out
     if end of round
     */
}
