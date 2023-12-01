package com.mancala.mancala.controller;

import com.mancala.mancala.model.Board;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private Board board = new Board();

    @GetMapping("/")
    public String gameState() {
        return board.toHTML();
    }

    @GetMapping("/move/{pitIndex}")
    public String makeMove(@PathVariable int pitIndex) {
        board.makeMove(pitIndex);

        return "Moved stones from pit " + pitIndex + ". <a href='/'>View Game</a>";
    }
}
