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
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class GameController {
    private final GameWebSocketHandler webSocketHandler;
    private final Map<Integer, Game> games = new ConcurrentHashMap<>();
    private final Map<String, Integer> sessionGameMap = new ConcurrentHashMap<>();
    private int lastGameId = -1;

    @Autowired
    public GameController(GameWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/start")
    public String startGame(HttpSession session) {
        String sessionId = session.getId();
        Game game;
        if (!sessionGameMap.containsKey(sessionId)) {
            game = getOrCreateGame();
            Player player = game.addPlayer();
            session.setAttribute("player", player);
            sessionGameMap.put(sessionId, game.getGameId());
        } else {
            game = games.get(sessionGameMap.get(sessionId));
        }
        return game.getHTML((Player) session.getAttribute("player"), sessionId);
    }

    private Game getOrCreateGame() {
        if (lastGameId == -1 || games.get(lastGameId).isFull()) {
            Game newGame = new Game();
            games.put(newGame.getGameId(), newGame);
            lastGameId = newGame.getGameId();
            return newGame;
        } else {
            return games.get(lastGameId);
        }
    }

    @GetMapping("/move/{pitIndex}")
    public String makeMove(@PathVariable int pitIndex, HttpSession session) {
        String sessionId = session.getId();
        Player player = (Player) session.getAttribute("player");
        if (player != null) {
            int gameId = sessionGameMap.get(sessionId);
            Game game = games.get(gameId);
            if (player == game.getCurrentPlayer()) {
                game.makeMove(pitIndex, player);
                try {
                    notifyOtherPlayersInGame(sessionId, gameId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return games.get(sessionGameMap.get(sessionId)).getHTML(player, sessionId);
    }

    private void notifyOtherPlayersInGame(String sessionId, int gameId) throws IOException {
        for (String otherSessionId : sessionGameMap.keySet()) {
            if (!otherSessionId.equals(sessionId) && sessionGameMap.get(otherSessionId).equals(gameId)) {
                webSocketHandler.notifyClient(otherSessionId);
            }
        }
    }

    @GetMapping("/game")
    public String gameBoard(HttpSession session) {
        String sessionId = session.getId();
        Player player = (Player) session.getAttribute("player");
        if (player != null) {
            return games.get(sessionGameMap.get(sessionId)).getHTML(player, sessionId);
        }
        return "redirect:/"; // Redirect to the start page if no player is set
    }
}
