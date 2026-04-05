package com.letter.princess.controllers.v1;


import com.letter.princess.game.Game;
import com.letter.princess.game.GameService;
import com.letter.princess.models.Player;
import com.letter.princess.views.GameView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/game")
public class PrincessController {

    private final GameService gameService;

    public PrincessController(GameService gameService) {
        this.gameService = gameService;
    }

    /* Game lobby methods */

    @PostMapping("/new")
    public void newGameLobby() {
        gameService.newLobby();
    }

    // TODO: should pass {gameId}
    @PostMapping("/add")
    public void addPlayer(@RequestBody String playerName) {
        gameService.addPlayer(playerName);
    }

    @PostMapping("/remove")
    public void removePlayer(@RequestBody String playerName) {
        gameService.removePlayer(playerName);
    }

    @PostMapping("/start")
    public void startGame() {
        gameService.startGame();
    }

    /* Game methods */

    @GetMapping("/{playerId}")
    public GameView getGameView(@PathVariable UUID playerId) {
        Game game = gameService.getGame();
        Player player = game.getPlayerById(playerId);
        return game.gameView(player);
    }

    // TODO: should we have sub-paths like action/play, action/end, etc?
    @PostMapping("/action/{playerId}")
    public GameView action(@PathVariable UUID playerId) {
        Game game = gameService.getGame();
        // TODO: figure out ID thing, it's not working
        Player player = game.getPlayerById(playerId);
        return game.gameView(player);
    }
}