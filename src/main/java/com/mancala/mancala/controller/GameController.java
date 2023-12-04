package com.mancala.mancala.controller;

import com.mancala.mancala.model.Game;
import com.mancala.mancala.model.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mancala.mancala.websocket.GameWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@RestController
public class GameController {
    private Game game;
    private final GameWebSocketHandler webSocketHandler;

    @Autowired
    public GameController(GameWebSocketHandler webSocketHandler) {
        this.game = new Game();
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/start")
    public String startGame(HttpSession session) {
        // Check if there's an ongoing game; if so, reset it
        if (game.isGameOver()) {
            game = new Game(); // Reset the game
        }

        // Assign a new player for the new game
        if (session.getAttribute("player") == null) {
            Player player = game.assignPlayer();
            session.setAttribute("player", player);
        }
        return game.getHTML((Player)session.getAttribute("player"));
    }

    @GetMapping("/move/{pitIndex}")
    public String makeMove(@PathVariable int pitIndex, HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        if (player != null && game.isPlayerTurn(player)) {
            game.makeMove(pitIndex, player);
            try {
                // Use the session ID for WebSocket notification
                String sessionId = session.getId();
                webSocketHandler.notifyClient(sessionId);
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }
        }
        return game.getHTML(player); // Return the updated state of the game
    }


    @GetMapping("/game")
    public String gameBoard(HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        if (player != null) {
            return game.getHTML(player);
        }
        return "redirect:/"; // Redirect to the start page if no player is set
    }
}
