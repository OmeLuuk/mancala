package com.mancala.mancala.model;

public class Game {
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

    public Player determineWinner() {
        int blueStoreCount = board.getStoreCount(Player.blue);
        int redStoreCount = board.getStoreCount(Player.red);

        if (blueStoreCount > redStoreCount) {
            return Player.blue;
        }
        else if (redStoreCount > blueStoreCount) {
            return Player.red;
        }
        else {
            return null; // or a specific value to indicate a tie
        }
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
    }
}
