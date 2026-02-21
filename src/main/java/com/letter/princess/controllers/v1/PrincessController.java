package com.letter.princess.controllers.v1;


import com.letter.princess.game.Game;
import com.letter.princess.game.GameService;
import com.letter.princess.models.Player;
import com.letter.princess.views.GameView;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class PrincessController {

    private final GameService gameService;

    public PrincessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{playerId}")
    public GameView getGameView(@PathVariable UUID playerId) {
        Game game = gameService.getGame();
        Player player = game.getPlayerById(playerId);
        return game.gameView(player);
    }

    @PostMapping("/start")
    public void startGame(@RequestBody List<String> playerNames) {
        gameService.startGame(playerNames);
    }
}