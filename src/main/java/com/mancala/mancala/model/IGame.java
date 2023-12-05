package com.mancala.mancala.model;

public interface IGame {
    String getHTML(Player player, String sessionId);
    void determineWinners();
    void makeMove(int pitIndex, Player player);
    boolean isGameOver();
    int getGameId();
    Player getCurrentPlayer();
    boolean isFull();
}
