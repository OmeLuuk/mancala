package com.mancala.mancala.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Game implements IGame{
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final int gameId;
    protected boolean gameOver = false;
    protected Player[] winners;
    protected IBoard board;
    protected Player currentPlayer;
    protected boolean isBluePlayerAssigned;
    protected boolean isRedPlayerAssigned;

    public Game() {
        this.gameId = idCounter.getAndIncrement();
        this.board = new Board();
        this.currentPlayer = Player.blue; // Blue starts
        this.isBluePlayerAssigned = false;
        this.isRedPlayerAssigned = false;
    }

    public String getHTML(Player player, String sessionId) {
        StringBuilder html = new StringBuilder();

        if (gameOver) {
            html.append("<h3>Game Over! Winner(s):</h3>");
            for (Player p : winners)
            {
                html.append("<h3><span style='color:").append(p).append(";'>").append(p).append("</span></h3>");
            }
            html.append("<form action='/start' method='get'><button type='submit'>Start New Game</button></form>");
            return html.toString();
        }

        // Add game-related information like current player's turn
        html.append("<h2 style='color:").append(currentPlayer).append(";'>Current Turn: ").append(currentPlayer).append("</h2>");

        // Show the current player's color
        html.append("<h3>You are playing as <span style='color:").append(player).append(";'>").append(player).append("</span></h3>");

        // Include the board's HTML
        html.append(this.board.toHTML(player == currentPlayer, player));

        html.append("<script>\n");
        html.append("const socket = new WebSocket('ws://localhost:8080/game-websocket?sessionId=").append(sessionId).append("');\n");
        html.append("socket.onmessage = function(event) {\n");
        html.append("    if (event.data === 'update') {\n");
        html.append("        window.location.href = '/game';\n");  // Redirect to the game endpoint
        html.append("    }\n");
        html.append("};\n");
        html.append("</script>\n");

        return html.toString();
    }

    public void determineWinners() {
        int maxScore = Integer.MIN_VALUE;
        List<Player> winnerList = new ArrayList<>();

        for (Player player : Player.values()) {
            int playerScore = board.getStoreCount(player);
            if (playerScore > maxScore) {
                maxScore = playerScore;
                winnerList.clear(); // Clear the list for the new leading player
                winnerList.add(player);
            } else if (playerScore == maxScore) {
                // Handle a tie by adding the player to the winnerList
                winnerList.add(player);
            }
        }

        winners = winnerList.toArray(new Player[0]);
    }

    // Method to add a player and return the assigned player
    public Player addPlayer() {
        if (!isBluePlayerAssigned) {
            isBluePlayerAssigned = true;
            return Player.blue;
        } else if (!isRedPlayerAssigned) {
            isRedPlayerAssigned = true;
            return Player.red;
        } else {
            throw new IllegalStateException("Game is full.");
        }
    }

    public void makeMove(int pitIndex, Player player) {
        if (player == currentPlayer) {
            boolean anotherTurn = board.makeMove(pitIndex, currentPlayer);
            if (!anotherTurn) {
                currentPlayer = currentPlayer.next();
            }
        }

        if (board.isGameOver())
        {
            gameOver = true;
            determineWinners();
        }
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public int getGameId() {
        return gameId;
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public boolean isFull() {
        return isBluePlayerAssigned && isRedPlayerAssigned;
    }
}
