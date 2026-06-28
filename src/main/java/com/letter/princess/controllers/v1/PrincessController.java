package com.letter.princess.controllers.v1;


import com.letter.princess.game.GameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}