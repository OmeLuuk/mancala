package com.mancala.mancala.model;

public interface IGame {
    String getHTML(Player player, String sessionId);
    Player assignPlayer();
    void determineWinners();
    boolean isPlayerTurn(Player player);
    void makeMove(int pitIndex, Player player);
    boolean isGameOver();
    int getGameId();
}
