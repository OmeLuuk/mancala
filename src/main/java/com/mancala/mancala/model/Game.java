package com.mancala.mancala.model;

public class Game {
    private final Board board;
    private Player currentPlayer;

    public Game() {
        this.board = new Board();
        this.currentPlayer = Player.blue; // blue starts
    }

    public String getHTML() {
        StringBuilder html = new StringBuilder();

        // Add game-related information like current player's turn
        html.append("<h2 style='color:").append(currentPlayer).append(";'>Current Turn: ").append(currentPlayer).append("</h2>");

        // Include the board's HTML
        html.append(this.board.toHTML());

        return html.toString();
    }

    public void makeMove(int pitIndex) {
        boolean anotherTurn = board.makeMove(pitIndex, currentPlayer);

        // Rotate to the next player unless they get another turn
        if (!anotherTurn) {
            currentPlayer = currentPlayer.next();
        }
    }
}
