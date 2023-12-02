package com.mancala.mancala.controller;

import com.mancala.mancala.model.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final Game game = new Game();

    @GetMapping("/")
    public String gameState() {
        return game.getHTML();
    }

    @GetMapping("/move/{pitIndex}")
    public String makeMove(@PathVariable int pitIndex) {
        game.makeMove(pitIndex);

        return "Moved stones from pit " + pitIndex + ". <a href='/'>View Game</a>";
    }
}
