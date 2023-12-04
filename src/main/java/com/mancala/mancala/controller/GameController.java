package com.mancala.mancala.controller;

import com.mancala.mancala.model.Game;
import com.mancala.mancala.model.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mancala.mancala.websocket.GameWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class GameController {
    private Game game;
    private final GameWebSocketHandler webSocketHandler;
    private final Map<Integer, Set<String>> gameSessionsMap = new ConcurrentHashMap<>();

    @Autowired
    public GameController(GameWebSocketHandler webSocketHandler) {
        this.game = new Game();
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/start")
    public String startGame(HttpSession session) {
        if (game.isGameOver()) {
            game = new Game();
        }

        if (session.getAttribute("player") == null) {
            Player player = game.assignPlayer();
            session.setAttribute("player", player);

            // Add the session ID to the game's session set
            int gameId = game.getGameId();
            gameSessionsMap.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet()).add(session.getId());
        }
        return game.getHTML((Player)session.getAttribute("player"), session.getId());
    }

    @GetMapping("/move/{pitIndex}")
    public String makeMove(@PathVariable int pitIndex, HttpSession session) {
        System.out.println("HTTP session ID: " + session.getId());
        Player player = (Player) session.getAttribute("player");
        if (player != null && game.isPlayerTurn(player)) {
            game.makeMove(pitIndex, player);
            try {
                int gameId = game.getGameId();
                // Notify all sessions in the game
                for (String otherSessionId : gameSessionsMap.getOrDefault(gameId, Set.of())) {
                    if (!Objects.equals(otherSessionId, session.getId()))
                        webSocketHandler.notifyClient(otherSessionId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return game.getHTML(player, session.getId());
    }

    @GetMapping("/game")
    public String gameBoard(HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        if (player != null) {
            return game.getHTML(player, session.getId());
        }
        return "redirect:/"; // Redirect to the start page if no player is set
    }
}
