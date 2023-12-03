package com.mancala.mancala.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private boolean gameOver = false;
    private Player[] winners;
    private final Board board;
    private Player currentPlayer;
    private boolean isBluePlayerAssigned;
    private boolean isRedPlayerAssigned;

    public Game() {
        this.board = new Board();
        this.currentPlayer = Player.blue; // Blue starts
        this.isBluePlayerAssigned = false;
        this.isRedPlayerAssigned = false;
    }

    public String getHTML(Player player) {
        StringBuilder html = new StringBuilder();

        if (gameOver) {
            html.append("<h3>Game Over! Winner(s):</h3>");
            for (Player p : winners)
            {
                html.append("<h3><span style='color:").append(player).append(";'>").append(player).append("</span></h3>");
            }
            return html.toString();
        }

        // Add game-related information like current player's turn
        html.append("<h2 style='color:").append(currentPlayer).append(";'>Current Turn: ").append(currentPlayer).append("</h2>");

        // Show the current player's color
        html.append("<h3>You are playing as <span style='color:").append(player).append(";'>").append(player).append("</span></h3>");

        // Include the board's HTML
        html.append(this.board.toHTML(player == currentPlayer, player));

        html.append("<a href='/game' style='color: black;'>Reload</a>");

        return html.toString();
    }


    public Player assignPlayer() {
        if (!isBluePlayerAssigned) {
            isBluePlayerAssigned = true;
            return Player.blue;
        }
        else if (!isRedPlayerAssigned) {
            isRedPlayerAssigned = true;
            return Player.red;
        }
        // Additional logic if needed for more players or handling errors
        return null;
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

    public boolean isPlayerTurn(Player player) {
        return currentPlayer == player;
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
}
