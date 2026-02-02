package com.letter.princess.game;

import com.letter.princess.models.Card;
import com.letter.princess.models.Deck;
import com.letter.princess.models.Player;
import com.letter.princess.views.CardView;
import com.letter.princess.views.GameView;
import com.letter.princess.views.KnownCard;
import com.letter.princess.views.PlayerView;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Represents a complete game of Princess
 */
@AllArgsConstructor
public class Game {

    @Getter // TODO: probably shouldn't be a getter, need a defensive copy
    private final List<Player> players;
    @Getter
    private final int numTokensToWin;
    private final Deck deck;
    private final Card discardedCard;
    // TODO: add support for 2-player game (3 discarded cards, faceup)
    @Getter
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
    // TODO: fix how it gets currentPlayer and otherPlayer views
    // TODO: add tests
    public GameView gameView(Player player) {
        return new GameView(currentRound, numTokensToWin, getSelfView(player), Collections.emptyList(), deck.getSize());
    }

    // TODO: add tests, also better way to check player equality
    private PlayerView getSelfView(Player player) {
        return new PlayerView(player.getName(),
                getHandView(player),
                Collections.emptyList(),
                player.getNumTokens(),
                player.isInRound()
                );
    }

    private List<CardView> getHandView(Player player) {
        return player.getHand().getCardsInHand().stream().map(c -> (CardView) new KnownCard(c)).toList();
    }
}
